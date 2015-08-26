package testOffheap


/**
 * Created by Aleksey Voronets on 26.08.15.
 */
class UnsafeStockExchange extends StockExchange {
    import UnsafeTrade._

    def order(ticket: Int, amount: Int, price: Int, buy: Boolean) =  synchronized {
        recordsCount += 1
        val trade = get(recordsCount)
        trade.setTicket(ticket)
        trade.setAmount(amount)
        trade.setPrice(price)
        trade.setBuy(buy)
    }


    def dayBalance() = synchronized {
        var balance = 0
        var i = 0
        while(i < recordsCount){
            val trade = get(i)
            balance += trade.getAmount() * trade.getPrice() * (if(trade.isBuy) 1 else -1)
            i += 1
        }
        balance
    }

    private val address = unsafe.allocateMemory(StockExchange.TRADES_PER_DAY * UnsafeTrade.getObjectSize())
    private val flyweight = UnsafeTrade

    private def get(index: Int) = {
        val offset: Long = address + (index * UnsafeTrade.getObjectSize())
        flyweight.setObjectOffset(offset)
        flyweight
    }

    def destroy() = unsafe.freeMemory(address)

    private var recordsCount = 0
}


private object UnsafeTrade {
    val unsafe = SunMisc.UNSAFE

    private var objectOffset = 0L

    private var offset = 0L

    private val ticketOffset = {offset += 0L; offset}

    private val amountOffset = {offset += 4L; offset}

    private val priceOffset = {offset += 4L; offset}

    private val buyOffset = {offset += 4L; offset}

    private val objectSize = {offset += 1L; offset}

    def getObjectSize() = objectSize

    def setObjectOffset(offset: Long) = UnsafeTrade.objectOffset = offset

    def setTicket(ticket: Int) = unsafe.putInt(UnsafeTrade.objectOffset + UnsafeTrade.ticketOffset, ticket)

    def getPrice() = unsafe.getInt(UnsafeTrade.objectOffset + UnsafeTrade.priceOffset)

    def setPrice(price: Int) = unsafe.putInt(UnsafeTrade.objectOffset + UnsafeTrade.priceOffset, price)

    def getAmount() = unsafe.getInt(UnsafeTrade.objectOffset + UnsafeTrade.amountOffset)

    def setAmount(quantity: Int)  = unsafe.putInt(UnsafeTrade.objectOffset + UnsafeTrade.amountOffset, quantity)

    def isBuy() = unsafe.getByte(UnsafeTrade.objectOffset + UnsafeTrade.buyOffset) == 1

    def setBuy(buy: Boolean) = unsafe.putByte(UnsafeTrade.objectOffset + UnsafeTrade.buyOffset, (if(buy) 1.toByte else 0.toByte))
}
