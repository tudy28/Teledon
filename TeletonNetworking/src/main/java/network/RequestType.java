package network;

import model.Donatie;
import model.Donator;
import model.Voluntar;

public enum RequestType {
    LOGIN, LOGOUT,
    ADD_DONATIE, ADD_DONATOR,
    UPDATE_CAZ,
    GET_DONATOR, GET_ALL_DONATORI, GET_PHONE, GET_DONATOR_BY_NAME,GET_CAZURI,GET_SINGLE_DONATOR,GET_SINGLE_CAZ

}