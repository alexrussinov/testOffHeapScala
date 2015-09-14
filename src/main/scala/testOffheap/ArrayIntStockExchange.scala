package testOffheap

/**
 * Created by Aleksey Voronets on 26.08.15.
 */
class ArrayIntStockExchange extends StockExchange{

    private var recordCount = 0

    private val flyweight = ArrayTrade

    private val orders = Array.fill(StockExchange.TRADES_PER_DAY * flyweight.getObjectSize)(0)

    override def order(ticket: Int, amount: Int, price: Int, buy: Boolean) =
    {
        val trade  = get(recordCount)
        trade.setTicket(ticket)
        trade.setAmount(amount)
        trade.setPrice(price)
        trade.setBuy(buy)
        recordCount += 1
    }

    def dayBalance: Double = {
        var balance: Double = 0
        var i: Int = 0
        while (i < recordCount) {
            val trade = get(i)
            balance += trade.getAmount * trade.getPrice * (if (trade.isBuy) 1 else -1)
            i += 1
        }
        balance
    }

    private def get(index: Int) = {
        val offset: Int = index * flyweight.getObjectSize
        flyweight.setObjectOffset(offset)
        flyweight
    }

    private object ArrayTrade {
        private var offset: Int = 0

        private final val ticketOffset: Int = offset

        private final val amountOffset: Int = {
            offset += 1
            offset
        }
        private final val priceOffset: Int = {
            offset += 1
            offset
        }
        private final val buyOffset: Int = {
            offset += 1
            offset
        }
        private final val objectSize: Int = {
            offset += 1
            offset
        }
        private var objectOffset: Int = 0

        def getObjectSize: Int = objectSize

        def setObjectOffset(objOffset: Int) = objectOffset = objOffset

        def setTicket(ticket: Int) = orders(objectOffset + ticketOffset) = ticket

        def getPrice = orders(objectOffset + priceOffset)

        def setPrice(price: Int) = orders(objectOffset + priceOffset) = price

        def getAmount = orders(objectOffset + amountOffset)
        
        def setAmount(quantity: Int) = orders(objectOffset + amountOffset) = quantity

        def isBuy: Boolean = orders(objectOffset + buyOffset) == 1

        def setBuy(buy: Boolean) = orders(objectOffset + buyOffset) = (if (buy) 1 else 0)
    }
}
