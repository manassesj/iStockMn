package bsi.mpoo.istock.services;

public class Constants {

    public static class Status {
        public static final int ACTIVE = 1;
        public static final int INACTIVE = 2;
        public static final int FIRST_ACCESS_FOR_USER = 3;
    }

    public static class Image{
        public static final int REQUEST_CODE = 1;
        public static final int ORIENTATION_OUT_OF_BOUNDS = 9;
        public static final int REQUIRED_SIZE = 100;
        public static final int SCALE_BASE = 1;
        public static final int OFFSET = 0;
        public static final int QUALITY = 70;
        public static final int SAFE_ANGLE = 0;
        public static final int FIRST_PIXEL_CORDINATE_SOURCE_X = 0;
        public static final int FIRST_PIXEL_CORDINATE_SOURCE_Y = 0;
    }

    public static class UserTypes{
        public static final int ADMINISTRATOR = 1;
        public static final int SALESMAN = 2;
        public static final int PRODUCER = 3;
        public static final int IS_THE_ADMINISTRATOR = -1;
    }

    public static class BundleKeys {
        public static final String USER = "user";
        public static final String CLIENT = "client";
        public static final String PRODUCT = "product";
        public static final String EMAIL = "email";
        public static final String SETTINGS = "settings";
        public static final String ITEMS = "items";
        public static final String ORDER = "order";
    }

    public static class MaskTypes {
        public static final String PHONE = "(NN) N NNNN-NNNN";
    }

    public static class Session {
        public static final int REMEMBER = 1;
        public static final int NOT_TO_REMEMBER = 0;
        public static final int POSITION_USER = 1;
    }

    public static class Activity{
        public static final String TEXT_PLAIN = "text/plain";
    }

    public static class SettingsHelper{
        public static final int COMPANY = 1;
        public static final int NAME = 2;
        public static final int EMAIL = 3;
        public static final int PASSWORD = 4;
        public static final int DELETE = 5;
    }

    public static class Order{
        public static final int DELIVERED = 1;
        public static final int NOT_DELIVERED = 0;
        public static final String ITEMS = "items";
    }

    public static class Date{
        public static final String FORMAT_DATE = "dd/MM/yyyy";
    }

}
