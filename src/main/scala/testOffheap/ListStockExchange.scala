package testOffheap

import scala.annotation.tailrec

/**
 * Created by Aleksey Voronets on 14.09.15.
 */
class ListStockExchange extends StockExchange {
    var orders: List[Trade] = Nil

    override def order(ticket: Int, amount: Int, price: Int, buy: Boolean): Unit = orders = Trade(ticket, amount, price, buy) :: orders

    override def dayBalance: Double = {
        @tailrec
        def calculate(acc: Double, orders: List[Trade]): Double =
        {
            if(orders.isEmpty) acc
            else calculate(acc + orders.head.amount * orders.head.price * (if(orders.head.buy)  1 else -1), orders.tail)
        }
        calculate(0.0, orders)
    }
}
