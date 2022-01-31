package com.es.phoneshop.model.product.price;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class PriceHistory {

    private List<LocalDate> dates;
    private List<BigDecimal> prices;

    public PriceHistory(List<LocalDate> dates, List<BigDecimal> prices) {
        this.dates = dates;
        this.prices = prices;
    }

    public List<LocalDate> getDates() {
        return dates;
    }

    public void setDates(List<LocalDate> dates) {
        this.dates = dates;
    }

    public List<BigDecimal> getPrices() {
        return prices;
    }

    public void setPrices(List<BigDecimal> prices) {
        this.prices = prices;
    }

    @Override
    public String toString() {
        return "PriceHistory{" +
                "dates=" + dates +
                ", prices=" + prices +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PriceHistory that = (PriceHistory) o;
        return Objects.equals(dates, that.dates) && Objects.equals(prices, that.prices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dates, prices);
    }
}
