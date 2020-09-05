package exchange.core2.orderbook.events;

import java.util.Objects;

public class ReduceEvent {

    private final long reducedVolume;
    private final long price;
    private final long reservedBidPrice;

    public ReduceEvent(long reducedVolume,
                       long price,
                       long reservedBidPrice) {

        this.reducedVolume = reducedVolume;
        this.price = price;
        this.reservedBidPrice = reservedBidPrice;
    }

    public long getReducedVolume() {
        return reducedVolume;
    }

    public long getPrice() {
        return price;
    }

    public long getReservedBidPrice() {
        return reservedBidPrice;
    }

    @Override
    public String toString() {
        return "ReduceEvent{" +
                "reducedVolume=" + reducedVolume +
                ", price=" + price +
                ", reservedBidPrice=" + reservedBidPrice +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReduceEvent that = (ReduceEvent) o;
        return reducedVolume == that.reducedVolume &&
                price == that.price &&
                reservedBidPrice == that.reservedBidPrice;
    }

    @Override
    public int hashCode() {
        return Objects.hash(reducedVolume, price, reservedBidPrice);
    }
}
