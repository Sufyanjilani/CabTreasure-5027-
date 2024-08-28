package base.models;

/**
 * @author: Kumail Raza Lakhani on 09/08/2016.
 */
public class UserModel {

    private String id, isverify, code, ip, name, email, mobileno, password;

/*
 *	@author: Kumail Raza Lakhani
 *	Date: 30-August-2016
 */
    private static String mUserId, mName, mEmail;

    public static String getUserId() {
        return mUserId;
    }

    public static void setUserName(String userName) {
        mName = userName;
    }

    public static String getUserName() {
        return mName;
    }

    public static void setUserEmail(String userEmail) {
        mUserId = userEmail;
    }

    public static String getUserEmail() {
        return mEmail;
    }

    public static void setUserId(String userId) {
        mUserId = userId;
    }

/*
 *	Date: 30-August-2016
 *	END ->
 */


    public void setmobileno(String no) {
        this.mobileno = no;
    }

    public String getmobileno() {
        return mobileno;
    }
    public void setip(String no) {
        this.ip = no;
    }

    public String getip() {
        return ip;
    }



    public void setEmail(String no) {
        this.email = no;
    }

    public String getEmail() {
        return email;
    }



    public void setname(String no) {
        this.name = no;
    }

    public String getname() {
        return name;
    }


}
