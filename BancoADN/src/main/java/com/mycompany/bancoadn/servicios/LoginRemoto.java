package com.mycompany.bancoadn.servicios;

public class LoginRemoto {

    private static BancoADN banco =
            new BancoADN();

    public static String login(
            String usuario,
            String password
    ) {

        return banco.login(
                usuario,
                password
        );
    }
}