package bsi.mpoo.istock.services;

public class Exceptions {

    public static class EmailAlreadyRegistered extends Exception{
        private ExceptionsEnum error;
        private int idError;

        public EmailAlreadyRegistered(){
            this.error = ExceptionsEnum.EMAIL_ALREADY_REGISTERED;
            this.idError = this.error.getValue();
        }

        public String getStringError(){
            return this.error.toString();
        }

        public int getIdError() {
            return this.idError;
        }
    }

    public static class ClientAlreadyRegistered extends Exception{
        private ExceptionsEnum error;
        private int idError;

        public ClientAlreadyRegistered(){
            this.error = ExceptionsEnum.CLIENT_ALREADY_REGISTERED;
            this.idError = this.error.getValue();
        }

        public String getStringError(){
            return this.error.toString();
        }

        public int getIdError() {
            return this.idError;
        }
    }

    public static class ClientNotRegistered extends Exception{
        private ExceptionsEnum error;
        private int idError;

        public ClientNotRegistered(){
            this.error = ExceptionsEnum.CLIENT_NOT_REGISTERED;
            this.idError = this.error.getValue();
        }

        public String getStringError(){
            return this.error.toString();
        }

        public int getIdError() {
            return this.idError;
        }
    }

    public static class ProductAlreadyRegistered extends Exception{
        private ExceptionsEnum error;
        private int idError;

        public ProductAlreadyRegistered(){
            this.error = ExceptionsEnum.PRODUCT_ALREADY_REGISTERED;
            this.idError = this.error.getValue();
        }

        public String getStringError(){
            return this.error.toString();
        }

        public int getIdError() {
            return this.idError;
        }
    }

    public static class ProductNotRegistered extends Exception{
        private ExceptionsEnum error;
        private int idError;

        public ProductNotRegistered(){
            this.error = ExceptionsEnum.PRODUCT_NOT_REGISTERED;
            this.idError = this.error.getValue();
        }

        public String getStringError(){
            return this.error.toString();
        }

        public int getIdError() {
            return this.idError;
        }
    }

    public static class UserNotRegistered extends Exception{
        private ExceptionsEnum error;
        private int idError;

        public UserNotRegistered(){
            this.error = ExceptionsEnum.USER_NOT_REGISTERED;
            this.idError = this.error.getValue();
        }

        public String getStringError(){
            return this.error.toString();
        }

        public int getIdError() {
            return this.idError;
        }
    }

    public static class OrderNotRegistered extends Exception {

        private ExceptionsEnum error;
        private int idError;

        public OrderNotRegistered(){
            this.error = ExceptionsEnum.ORDER_NOT_REGISTERED;
            this.idError = this.error.getValue();
        }

        public String getStringError(){
            return this.error.toString();
        }

        public int getIdError() {
            return this.idError;
        }

    }
}
