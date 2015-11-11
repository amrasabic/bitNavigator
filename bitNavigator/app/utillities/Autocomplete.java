package utillities;

import models.Reservation;

/**
 * Created by hajrudin.sehic on 23/10/15.
 */
public class Autocomplete {
    private static final int ONE_MINUTE = 60000;
    private static final int ONE_HOUR = 3600000;
    private static final int ONE_DAY = 86400000;

    /**
     * Starts a thread every hour, thread calls method that completes
     * reservation if reservationCheckoutDate passed currentDate.
     */
    public static void completeReservations() {

        Runnable checkDates = () -> {

            while (!Thread.interrupted()) {
                try {
                    Reservation.checkReservationExpiration();
                    Thread.sleep(ONE_HOUR);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(checkDates).start();
    }

}
