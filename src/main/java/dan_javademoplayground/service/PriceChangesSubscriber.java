package dan_javademoplayground.service;


import dan_javademoplayground.model.PriceChangedEvent;

public interface PriceChangesSubscriber {

    String identifier();
    void onPriceChanged(PriceChangedEvent e);

}
