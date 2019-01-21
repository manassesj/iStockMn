package bsi.mpoo.istock.services;

import android.content.Context;
import bsi.mpoo.istock.data.session.SessionDAO;
import bsi.mpoo.istock.domain.Administrator;
import bsi.mpoo.istock.domain.Producer;
import bsi.mpoo.istock.domain.Salesman;
import bsi.mpoo.istock.domain.Session;
import bsi.mpoo.istock.domain.User;
import bsi.mpoo.istock.services.user.UserServices;

public class SessionServices {
    private SessionDAO sessionDAO;
    private UserServices userServices;
    public SessionServices(Context context){
        sessionDAO = new SessionDAO(context);
        userServices = new UserServices(context);
    }

    private void update(Session session){
        sessionDAO.update(session);
    }

    public void refreshSession(){
        Session.setInstance(getSession());
    }

    public Session getSession(){
        return sessionDAO.get();
    }

    public void clearSession(){
        sessionDAO.clearSession();
    }

    public void updateSession(Object account, int remember){
        User user =  userServices.getUserFromDomainType(account);
        Administrator administrator;
        if (account instanceof Administrator){
            administrator = (Administrator) account;
        } else {
            User adminUser = userServices.getUserById(user.getAdministrator());
            administrator = (Administrator) userServices.getUserInDomainType(adminUser);
        }
        if (account instanceof Administrator){
            Session.getInstance().setAdministrator((Administrator) account);
            Session.getInstance().setAccount((Administrator) account);
        } else if (account instanceof Salesman){
            Session.getInstance().setAdministrator((administrator));
            Session.getInstance().setAccount((Salesman) account);
        } else {
            Session.getInstance().setAdministrator((administrator));
            Session.getInstance().setAccount((Producer) account);
        }
        Session.getInstance().setId_user(user.getId());
        Session.getInstance().setRemember(remember);
        update(Session.getInstance());
    }
}
