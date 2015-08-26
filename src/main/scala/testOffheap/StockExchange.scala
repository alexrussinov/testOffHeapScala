package testOffheap

/**
 * Created by Aleksey Voronets on 26.08.15.
 */
trait StockExchange {
    def order(ticket: Int, amount: Int, price: Int, buy: Boolean): Unit
    def dayBalance: Double
}

object StockExchange
{
    val TRADES_PER_DAY: Int = 50000000
}