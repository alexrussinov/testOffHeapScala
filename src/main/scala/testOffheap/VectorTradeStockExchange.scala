package testOffheap

import scala.annotation.tailrec

/**
 * Created by Aleksey Voronets on 26.08.15.
 */
class VectorTradeStockExchange extends StockExchange{
    private var orders = Vector.fill(StockExchange.TRADES_PER_DAY * 4)(0)

    private var pointer = 0

    private val offset = 4

    override def order(ticket: Int, amount: Int, price: Int, buy: Boolean) =
    {
        orders = orders.updated(pointer, ticket).updated(pointer + 1, amount).updated(pointer + 2, price)
            .updated(pointer + 3,  if(buy) 1 else 0)
        pointer = pointer + offset
    }

    override def dayBalance: Double = {
        @tailrec
        def calculate(acc: Double, pointer: Int): Double = {
            if(pointer == StockExchange.TRADES_PER_DAY * offset)
                acc
            else
                calculate(acc + orders(pointer + 1) * orders(pointer + 2) * (if(orders(pointer + 3) == 1) 1 else -1), pointer + offset + 1)
        }
        calculate(0.0, 0)
    }
}