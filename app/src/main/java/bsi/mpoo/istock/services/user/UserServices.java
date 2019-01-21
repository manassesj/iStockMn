package bsi.mpoo.istock.services.user;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import bsi.mpoo.istock.data.Contract;
import bsi.mpoo.istock.data.user.UserDAO;
import bsi.mpoo.istock.domain.Administrator;
import bsi.mpoo.istock.domain.Producer;
import bsi.mpoo.istock.domain.Product;
import bsi.mpoo.istock.domain.Salesman;
import bsi.mpoo.istock.domain.User;
import bsi.mpoo.istock.gui.AlertDialogGenerator;
import bsi.mpoo.istock.services.Constants;
import bsi.mpoo.istock.services.Encryption;
import bsi.mpoo.istock.services.Exceptions;
import bsi.mpoo.istock.services.Exceptions.EmailAlreadyRegistered;

public class UserServices {

    private UserDAO userDAO;

    public UserServices(Context context){
        this.userDAO = new UserDAO(context);
    }

    private boolean isUserRegistered(User user){
        User searchedUser = userDAO.getUserByEmail(user.getEmail());
        return searchedUser != null;
    }

    public void registerUser(User user) throws Exception {

        if (isUserRegistered(user)){
            throw new EmailAlreadyRegistered();
        } else {
            user.setPassword(Encryption.encrypt(user.getPassword()));
            userDAO.insertUser(user);
        }
    }

    public User login(User user){
        User searchedUser = userDAO.getUserByEmail(user.getEmail());
        if (searchedUser != null){
            if (searchedUser.getStatus() == Constants.Status.ACTIVE ||
                    searchedUser.getStatus() == Constants.Status.FIRST_ACCESS_FOR_USER){
                if (searchedUser.getPassword().equals(Encryption.encrypt(user.getPassword()))){
                    return searchedUser;
                } else {
                    return null;
                }
            }
            return null;
        }
        return null;
    }

    public ArrayList<User> getAcitiveUsersAsc(Administrator administrator){
        return (ArrayList<User>) userDAO.getActiveUsersByAdmId(administrator,Contract.ASC);
    }

    public ArrayList<User> getAcitiveUsersDesc(Administrator administrator){
        return (ArrayList<User>) userDAO.getActiveUsersByAdmId(administrator,Contract.DESC);
    }

    public ArrayList<User> getUsersAsc(Administrator administrator){
        return (ArrayList<User>) userDAO.getUsersByAdmId(administrator, Contract.ASC);
    }

    public ArrayList<User> getUsersDesc(Administrator administrator){
        return (ArrayList<User>) userDAO.getUsersByAdmId(administrator, Contract.DESC);
    }

    public ArrayList<Object> listUserToDomainTypeList(List<User> userList){
        ArrayList<Object> arrayList = new ArrayList<>();
        for (User user: userList ){
            switch (user.getType()){
                case Constants.UserTypes.ADMINISTRATOR:
                    Administrator administrator = new Administrator();
                    administrator.setUser(user);
                    arrayList.add(administrator);
                    break;
                case Constants.UserTypes.SALESMAN:
                    Salesman salesman = new Salesman();
                    salesman.setUser(user);
                    arrayList.add(salesman);
                    break;
                case Constants.UserTypes.PRODUCER:
                    Producer producer = new Producer();
                    producer.setUser(user);
                    arrayList.add(producer);
                    break;
            }
        }
        return arrayList;
    }

    public Object getUserInDomainType(User user) {
        switch (user.getType()) {
            case Constants.UserTypes.ADMINISTRATOR:
                Administrator administrator = new Administrator();
                administrator.setUser(user);
                return administrator;
            case Constants.UserTypes.SALESMAN:
                Salesman salesman = new Salesman();
                salesman.setUser(user);
                return salesman;
            case Constants.UserTypes.PRODUCER:
                Producer producer = new Producer();
                producer.setUser(user);
                return producer;
        }
        return null;
    }

    public User getUserById(long id){
        return userDAO.getUserById(id);
    }

    public void updateUser(User user) throws Exception{
        try {
            userDAO.updateUser(user);
        } catch (Exception error){
            throw new Exceptions.UserNotRegistered();
        }
    }

    public void disableUser(User user) throws Exception{
        if (isUserRegistered(user)){
            userDAO.disableUser(user);
        } else {
            throw new Exceptions.UserNotRegistered();
        }
    }

    public User getUserFromDomainType(Administrator administrator){
        return administrator.getUser();
    }

    public User getUserFromDomainType(Salesman salesman){
        return salesman.getUser();
    }

    public User getUserFromDomainType(Producer producer){
        return producer.getUser();
    }

    public User getUserFromDomainType(Object account){
        if (account instanceof Administrator){
            return ((Administrator) account).getUser();
        } else if (account instanceof Salesman){
            return ((Salesman) account).getUser();
        } else if (account instanceof Producer){
            return  ((Producer) account).getUser();
        } else {
            return null;
        }

    }

    public void deleteCompany(User user){
        Object account = getUserInDomainType(user);
        if (account instanceof Administrator){
            Administrator administrator = (Administrator) account;
            List<User> users = getAcitiveUsersAsc(administrator);
            for (User userCompany: users){
                    userDAO.disableUser(userCompany);
            }
            userDAO.disableUser(user);
        }
    }
}



