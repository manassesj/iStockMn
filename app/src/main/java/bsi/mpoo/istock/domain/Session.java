package bsi.mpoo.istock.domain;

public class Session {
    private static Session instance = new Session();
    private static Object account;
    private static int remember;
    private static long id_user;
    private static Administrator administrator;

    public Object getAccount() {
        return account;
    }

    public void setAccount(Administrator user) {
        Session.account = user;
        Session.administrator = user;
    }

    public void setAccount(Salesman user){
        Session.account = user;
    }

    public void setAccount(Producer user){
        Session.account = user;
    }

    public int getRemember() {
        return remember;
    }

    public void setRemember(int remember) {
        Session.remember = remember;
    }

    public long getId_user() {
        return id_user;
    }

    public void setId_user(long id_user) {
        Session.id_user = id_user;
    }

    public static Session getInstance(){
        return instance;
    }

    public Administrator getAdministrator() {
        return administrator;
    }

    public void setAdministrator(Administrator administrator) {
        Session.administrator = administrator;
    }

    public static void setInstance(Session session){
        Session.instance = session;
    }
}
