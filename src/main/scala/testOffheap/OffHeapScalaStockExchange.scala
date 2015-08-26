package testOffheap

import scala.offheap._

/**
 * Created by Aleksey Voronets on 26.08.15.
 */
@data class OffHeapTrade(ticket: Int, amount: Int, price: Int, buy: Boolean)
{
    def balance = amount * price * (if(buy) 1 else -1)
}

class OffHeapScalaStockExchange extends StockExchange {

    implicit val alloc = malloc

    private var pointer = 0

    val orders = EmbedArray.uninit[OffHeapTrade](StockExchange.TRADES_PER_DAY)

    override def order(ticket: Int, amount: Int, price: Int, buy: Boolean): Unit = {
        orders(pointer) = OffHeapTrade(ticket, amount, price, buy)
        pointer += 1
    }

    override def dayBalance: Double = {
        var result = 0.0
        var i = 0
        while(i < orders.size){
            val order = orders(i)
            result += order.balance
            i += 1
        }
        result
    }
}


