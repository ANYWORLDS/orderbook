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
package exchange.core2.tests.util;

import com.google.common.base.Strings;
import exchange.core2.orderbook.L2MarketData;
import exchange.core2.orderbook.events.QueryResponseL2Data;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

public class L2MarketDataHelper {

    private long[] askPrices;
    private long[] askVolumes;
    private long[] askOrders;
    private long[] bidPrices;
    private long[] bidVolumes;
    private long[] bidOrders;

    public L2MarketDataHelper(L2MarketData l2) {
        askPrices = Arrays.copyOf(l2.askPrices, l2.askPrices.length);
        askVolumes = Arrays.copyOf(l2.askVolumes, l2.askVolumes.length);
        askOrders = Arrays.copyOf(l2.askOrders, l2.askOrders.length);
        bidPrices = Arrays.copyOf(l2.bidPrices, l2.bidPrices.length);
        bidVolumes = Arrays.copyOf(l2.bidVolumes, l2.bidVolumes.length);
        bidOrders = Arrays.copyOf(l2.bidOrders, l2.bidOrders.length);
    }

    public L2MarketData build() {
        return new L2MarketData(
                askPrices,
                askVolumes,
                askOrders,
                bidPrices,
                bidVolumes,
                bidOrders
        );
    }

    public long aggregateBuyBudget(long size) {

        long budget = 0;
        for (int i = 0; i < askPrices.length; i++) {
            long v = askVolumes[i];
            long p = askPrices[i];
            if (v < size) {
                budget += v * p;
                size -= v;
            } else {
                return budget + size * p;
            }
        }

        throw new IllegalArgumentException("Can not collect size " + size);
    }

    public long aggregateSellExpectation(long size) {

        long expectation = 0;
        for (int i = 0; i < bidPrices.length; i++) {
            long v = bidVolumes[i];
            long p = bidPrices[i];
            if (v < size) {
                expectation += v * p;
                size -= v;
            } else {
                return expectation + size * p;
            }
        }

        throw new IllegalArgumentException("Can not collect size " + size);
    }

    public L2MarketDataHelper setAskPrice(int pos, long askPrice) {
        askPrices[pos] = askPrice;
        return this;
    }

    public L2MarketDataHelper setBidPrice(int pos, long bidPrice) {
        bidPrices[pos] = bidPrice;
        return this;
    }

    public L2MarketDataHelper setAskVolume(int pos, long askVolume) {
        askVolumes[pos] = askVolume;
        return this;
    }

    public L2MarketDataHelper setBidVolume(int pos, long bidVolume) {
        bidVolumes[pos] = bidVolume;
        return this;
    }

    public L2MarketDataHelper decrementAskVolume(int pos, long askVolumeDiff) {
        askVolumes[pos] -= askVolumeDiff;
        return this;
    }

    public L2MarketDataHelper decrementBidVolume(int pos, long bidVolumeDiff) {
        bidVolumes[pos] -= bidVolumeDiff;
        return this;
    }

    public L2MarketDataHelper setAskPriceVolume(int pos, long askPrice, long askVolume) {
        askVolumes[pos] = askVolume;
        askPrices[pos] = askPrice;
        return this;
    }

    public L2MarketDataHelper setBidPriceVolume(int pos, long bidPrice, long bidVolume) {
        bidVolumes[pos] = bidVolume;
        bidPrices[pos] = bidPrice;
        return this;
    }

    public L2MarketDataHelper decrementAskOrdersNum(int pos) {
        askOrders[pos]--;
        return this;
    }

    public L2MarketDataHelper decrementBidOrdersNum(int pos) {
        bidOrders[pos]--;
        return this;
    }

    public L2MarketDataHelper incrementAskOrdersNum(int pos) {
        askOrders[pos]++;
        return this;
    }

    public L2MarketDataHelper incrementBidOrdersNum(int pos) {
        bidOrders[pos]++;
        return this;
    }

    public L2MarketDataHelper removeAsk(int pos) {
        askPrices = ArrayUtils.remove(askPrices, pos);
        askVolumes = ArrayUtils.remove(askVolumes, pos);
        askOrders = ArrayUtils.remove(askOrders, pos);
        return this;
    }

    public L2MarketDataHelper removeAllAsks() {
        askPrices = new long[0];
        askVolumes = new long[0];
        askOrders = new long[0];
        return this;
    }

    public L2MarketDataHelper removeBid(int pos) {
        bidPrices = ArrayUtils.remove(bidPrices, pos);
        bidVolumes = ArrayUtils.remove(bidVolumes, pos);
        bidOrders = ArrayUtils.remove(bidOrders, pos);
        return this;
    }

    public L2MarketDataHelper removeAllBids() {
        bidPrices = new long[0];
        bidVolumes = new long[0];
        bidOrders = new long[0];
        return this;
    }

    public L2MarketDataHelper insertAsk(int pos, long price, long volume) {
        askPrices = ArrayUtils.insert(pos, askPrices, price);
        askVolumes = ArrayUtils.insert(pos, askVolumes, volume);
        askOrders = ArrayUtils.insert(pos, askOrders, 1);
        return this;
    }

    public L2MarketDataHelper insertBid(int pos, long price, long volume) {
        bidPrices = ArrayUtils.insert(pos, bidPrices, price);
        bidVolumes = ArrayUtils.insert(pos, bidVolumes, volume);
        bidOrders = ArrayUtils.insert(pos, bidOrders, 1);
        return this;
    }

    public L2MarketDataHelper addAsk(long price, long volume) {
        askPrices = ArrayUtils.add(askPrices, price);
        askVolumes = ArrayUtils.add(askVolumes, volume);
        askOrders = ArrayUtils.add(askOrders, 1);
        return this;
    }

    public L2MarketDataHelper addBid(long price, long volume) {
        bidPrices = ArrayUtils.add(bidPrices, price);
        bidVolumes = ArrayUtils.add(bidVolumes, volume);
        bidOrders = ArrayUtils.add(bidOrders, 1);
        return this;
    }

    public boolean checkL2Data(final QueryResponseL2Data data) {

        if (data.getAsks().size() != askOrders.length) return false;
        if (data.getBids().size() != bidOrders.length) return false;

        for (int i = 0; i < askOrders.length; i++) {
            final QueryResponseL2Data.L2Record r = data.getAsks().get(i);
            if (r.getPrice() != askPrices[i]) return false;
            if (r.getOrders() != askOrders[i]) return false;
            if (r.getVolume() != askVolumes[i]) return false;
        }

        for (int i = 0; i < bidOrders.length; i++) {
            final QueryResponseL2Data.L2Record r = data.getBids().get(i);
            if (r.getPrice() != bidPrices[i]) return false;
            if (r.getOrders() != bidOrders[i]) return false;
            if (r.getVolume() != bidVolumes[i]) return false;
        }

        return true;
    }

    public static String dumpOrderBook(L2MarketData l2MarketData) {

        int askSize = l2MarketData.askSize;
        int bidSize = l2MarketData.bidSize;

        long[] askPrices = l2MarketData.askPrices;
        long[] askVolumes = l2MarketData.askVolumes;
        long[] askOrders = l2MarketData.askOrders;
        long[] bidPrices = l2MarketData.bidPrices;
        long[] bidVolumes = l2MarketData.bidVolumes;
        long[] bidOrders = l2MarketData.bidOrders;

        int priceWidth = maxWidth(2, Arrays.copyOf(askPrices, askSize), Arrays.copyOf(bidPrices, bidSize));
        int volWidth = maxWidth(2, Arrays.copyOf(askVolumes, askSize), Arrays.copyOf(bidVolumes, bidSize));
        int ordWith = maxWidth(2, Arrays.copyOf(askOrders, askSize), Arrays.copyOf(bidOrders, bidSize));

        StringBuilder s = new StringBuilder("Order book:\n");
        s.append(".")
                .append(Strings.repeat("-", priceWidth - 1))
                .append("ASKS")
                .append(Strings.repeat("-", volWidth + ordWith - 1))

                .append(".\n");
        for (int i = askSize - 1; i >= 0; i--) {
            String price = Strings.padStart(String.valueOf(askPrices[i]), priceWidth, ' ');
            String volume = Strings.padStart(String.valueOf(askVolumes[i]), volWidth, ' ');
            String orders = Strings.padStart(String.valueOf(askOrders[i]), ordWith, ' ');
            s.append(String.format("|%s|%s|%s|\n", price, volume, orders));
        }

        s.append("|")
                .append(Strings.repeat("-", priceWidth))
                .append("+")
                .append(Strings.repeat("-", volWidth))
                .append("+")
                .append(Strings.repeat("-", ordWith))
                .append("|\n");

        for (int i = 0; i < bidSize; i++) {
            String price = Strings.padStart(String.valueOf(bidPrices[i]), priceWidth, ' ');
            String volume = Strings.padStart(String.valueOf(bidVolumes[i]), volWidth, ' ');
            String orders = Strings.padStart(String.valueOf(bidOrders[i]), ordWith, ' ');
            s.append(String.format("|%s|%s|%s|\n", price, volume, orders));
        }
        s.append("'")
                .append(Strings.repeat("-", priceWidth - 1))
                .append("BIDS")
                .append(Strings.repeat("-", volWidth + ordWith - 1))
                .append("'\n");
        return s.toString();
    }

    private static int maxWidth(int minWidth, long[]... arrays) {
        return Arrays.stream(arrays)
                .flatMapToLong(Arrays::stream)
                .mapToInt(p -> String.valueOf(p).length())
                .max()
                .orElse(minWidth);
    }

    private static int maxWidth(int minWidth, int[]... arrays) {
        return Arrays.stream(arrays)
                .flatMapToInt(Arrays::stream)
                .map(p -> String.valueOf(p).length())
                .max()
                .orElse(minWidth);
    }


}
