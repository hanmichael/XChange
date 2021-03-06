package org.knowm.xchange.cryptopia.service;

import java.io.IOException;
import java.util.Collection;

import org.knowm.xchange.cryptopia.CryptopiaExchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.marketdata.Trades;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.MarketOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.dto.trade.UserTrades;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.exceptions.NotAvailableFromExchangeException;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;
import org.knowm.xchange.service.trade.TradeService;
import org.knowm.xchange.service.trade.params.CancelOrderByIdParams;
import org.knowm.xchange.service.trade.params.CancelOrderParams;
import org.knowm.xchange.service.trade.params.TradeHistoryParamCurrencyPair;
import org.knowm.xchange.service.trade.params.TradeHistoryParamLimit;
import org.knowm.xchange.service.trade.params.TradeHistoryParams;
import org.knowm.xchange.service.trade.params.orders.DefaultOpenOrdersParamCurrencyPair;
import org.knowm.xchange.service.trade.params.orders.OpenOrdersParamCurrencyPair;
import org.knowm.xchange.service.trade.params.orders.OpenOrdersParams;

public class CryptopiaTradeService extends CryptopiaTradeServiceRaw implements TradeService {
  public CryptopiaTradeService(CryptopiaExchange exchange) {
    super(exchange);
  }

  @Override
  public OpenOrders getOpenOrders() throws ExchangeException, NotAvailableFromExchangeException, NotYetImplementedForExchangeException, IOException {
    return new OpenOrders(getOpenOrders(null, null));
  }

  @Override
  public OpenOrders getOpenOrders(OpenOrdersParams params) throws ExchangeException, NotAvailableFromExchangeException, NotYetImplementedForExchangeException, IOException {
    CurrencyPair currencyPair = null;
    if (params instanceof OpenOrdersParamCurrencyPair) {
      currencyPair = ((OpenOrdersParamCurrencyPair) params).getCurrencyPair();
    }
    return new OpenOrders(getOpenOrders(currencyPair, null));
  }

  @Override
  public String placeMarketOrder(MarketOrder marketOrder) throws ExchangeException, NotAvailableFromExchangeException, NotYetImplementedForExchangeException, IOException {
    throw new NotAvailableFromExchangeException();
  }

  @Override
  public String placeLimitOrder(LimitOrder limitOrder) throws ExchangeException, NotAvailableFromExchangeException, NotYetImplementedForExchangeException, IOException {
    return submitTrade(limitOrder.getCurrencyPair(), limitOrder.getType(), limitOrder.getLimitPrice(), limitOrder.getOriginalAmount());
  }

  @Override
  public boolean cancelOrder(String orderId) throws ExchangeException, NotAvailableFromExchangeException, NotYetImplementedForExchangeException, IOException {
    return cancel(orderId);
  }

  @Override
  public boolean cancelOrder(CancelOrderParams orderParams) throws ExchangeException, NotAvailableFromExchangeException, NotYetImplementedForExchangeException, IOException {
    if (orderParams instanceof CancelOrderByIdParams) {
      CancelOrderByIdParams params = (CancelOrderByIdParams) orderParams;
      return cancel(params.orderId);
    } else {
      throw new IllegalStateException("Dont understand " + orderParams);
    }
  }

  @Override
  public UserTrades getTradeHistory(TradeHistoryParams params) throws IOException {
    CurrencyPair currencyPair = null;
    Integer limit = 100;

    if (params instanceof TradeHistoryParamCurrencyPair) {
      currencyPair = ((TradeHistoryParamCurrencyPair) params).getCurrencyPair();
    }

    if (params instanceof TradeHistoryParamLimit) {
      limit = ((TradeHistoryParamLimit) params).getLimit();
    }

    return new UserTrades(tradeHistory(currencyPair, limit), Trades.TradeSortType.SortByTimestamp);
  }

  @Override
  public TradeHistoryParams createTradeHistoryParams() {
    return new CryptopiaTradeHistoryParams();
  }

  @Override
  public OpenOrdersParams createOpenOrdersParams() {
    return new DefaultOpenOrdersParamCurrencyPair();
  }

  @Override
  public void verifyOrder(LimitOrder limitOrder) {
    throw new NotAvailableFromExchangeException();
  }

  @Override
  public void verifyOrder(MarketOrder marketOrder) {
    throw new NotAvailableFromExchangeException();
  }

  @Override
  public Collection<Order> getOrder(String... orderIds) throws ExchangeException, NotAvailableFromExchangeException, NotYetImplementedForExchangeException, IOException {
    throw new NotAvailableFromExchangeException();
  }

  public static class CryptopiaTradeHistoryParams implements TradeHistoryParamCurrencyPair, TradeHistoryParamLimit {

    private CurrencyPair currencyPair;
    private Integer limit;

    public CryptopiaTradeHistoryParams(CurrencyPair currencyPair, Integer limit) {
      this.currencyPair = currencyPair;
      this.limit = limit;
    }

    public CryptopiaTradeHistoryParams() {
    }

    @Override
    public void setCurrencyPair(CurrencyPair currencyPair) {
      this.currencyPair = currencyPair;
    }

    @Override
    public CurrencyPair getCurrencyPair() {
      return currencyPair;
    }

    @Override
    public void setLimit(Integer limit) {
      this.limit = limit;
    }

    @Override
    public Integer getLimit() {
      return limit;
    }
  }
}
