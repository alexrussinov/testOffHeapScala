package testOffheap

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer

/**
 * Created by Aleksey Voronets on 26.08.15.
 */
class ListBufferStockExchange extends StockExchange {
    val orders = ListBuffer[Trade]()

    override def order(ticket: Int, amount: Int, price: Int, buy: Boolean): Unit = orders += new Trade(ticket, amount, price, buy)

    override def dayBalance: Double = {
        @tailrec
        def calculate(acc: Double, orders: ListBuffer[Trade]): Double =
        {
            if(orders.isEmpty) acc
            else calculate(acc + orders.head.amount * orders.head.price * (if(orders.head.buy)  1 else -1), orders.tail)
        }
        calculate(0.0, orders)
    }
}