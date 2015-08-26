package testOffheap

import java.util

/**
 * Created by Aleksey Voronets on 26.08.15.
 */
class ArrayListStockExchange extends StockExchange{
    val orders: util.ArrayList[Trade] = new util.ArrayList()

    override def order(ticket: Int, amount: Int, price: Int, buy: Boolean): Unit = orders.add(new Trade(ticket, amount, price, buy))

    override def dayBalance: Double = {
        /*        @tailrec
                def calculate(acc: Double, orders: util.ArrayList[Trade]): Double =
                {
                    if(orders.isEmpty) acc
                    else calculate(acc + orders.head.amount * orders.head.price * (if(orders.head.buy)  1 else -1), orders.tail)
                }
                calculate(0.0, orders)*/

        var result = 0
        var i = 0
        while(i < orders.size){
            result += orders.get(i).amount * orders.get(i).price * (if(orders.get(i).buy) 1 else -1)
            i += 1
        }
        result
    }
}
