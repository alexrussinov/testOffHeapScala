package testOffheap

import scala.offheap.{malloc, EmbedArray, Region, Pool}

/**
 * Created by Aleksey Voronets on 26.08.15.
 */
class OffHeapScalaStockExchangeRegion extends StockExchange {
    implicit val props = Region.Props(Pool())

    private var pointer = 0

    var orders: EmbedArray[OffHeapTrade] =  Region{ implicit r =>
        EmbedArray.uninit[OffHeapTrade](StockExchange.TRADES_PER_DAY)
    }

    override def order(ticket: Int, amount: Int, price: Int, buy: Boolean): Unit = {
        Region { implicit r => orders(pointer) = OffHeapTrade(ticket, amount, price, buy) }
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
/*        alloc.free(orders.addr)*/
        result
    }
}
