/*
 * Copyright 2020 Maksim Zheravin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package exchange.core2.orderbook.naive;

import exchange.core2.orderbook.IOrder;
import exchange.core2.orderbook.OrderAction;

import java.util.Objects;

public final class NaivePendingOrder implements IOrder {


    public NaivePendingOrder(long orderId, long price, long size, long filled, long reserveBidPrice, OrderAction action, long uid, long timestamp) {
        this.orderId = orderId;
        this.price = price;
        this.size = size;
        this.filled = filled;
        this.reserveBidPrice = reserveBidPrice;
        this.action = action;
        this.uid = uid;
        this.timestamp = timestamp;
    }

    final long orderId;

    long price;

    long size;

    long filled;

    // new orders - reserved price for fast moves of GTC bid orders in exchange mode
    final long reserveBidPrice;

    // required for PLACE_ORDER only;
    final OrderAction action;

    final long uid;

    final long timestamp;

    @Override
    public long getPrice() {
        return price;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public long getFilled() {
        return filled;
    }

    @Override
    public long getUid() {
        return uid;
    }

    @Override
    public OrderAction getAction() {
        return action;
    }

    @Override
    public long getOrderId() {
        return orderId;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public long getReserveBidPrice() {
        return reserveBidPrice;
    }

    @Override
    public int stateHash() {
        return Objects.hash(orderId, action, price, size, reserveBidPrice, filled,
                //userCookie,
                uid);
    }

    @Override
    public String toString() {
        return "NaivePendingOrder{" +
                "orderId=" + orderId +
                ", price=" + price +
                ", size=" + size +
                ", filled=" + filled +
                ", reserveBidPrice=" + reserveBidPrice +
                ", action=" + action +
                ", uid=" + uid +
                ", timestamp=" + timestamp +
                '}';
    }
}
