package teleton.service;

import model.Caz;
import model.Donator;

public interface Observer {
    void notifyAddedDonation(Iterable<Caz> cazuri) throws Exception;

    void notifyAddedDonor(Iterable<Donator> donatori) throws Exception;
}
