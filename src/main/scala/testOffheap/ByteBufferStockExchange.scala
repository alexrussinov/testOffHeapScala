package testOffheap

import java.nio.{ByteOrder, ByteBuffer}

/**
 * Created by Aleksey Voronets on 26.08.15.
 */
class ByteBufferStockExchange(direct: Boolean) extends StockExchange {

    private var recordsCount: Int = 0
    private val flyweight = BufferTrade

    private val buffer: ByteBuffer = {
        if (direct) ByteBuffer.allocateDirect(StockExchange.TRADES_PER_DAY * flyweight.getObjectSize)
        else ByteBuffer.allocate(StockExchange.TRADES_PER_DAY * flyweight.getObjectSize)
    }

    buffer.order(ByteOrder.nativeOrder())

    def order(ticket: Int, amount: Int, price: Int, buy: Boolean) {
        val trade  = get(recordsCount)
        trade.setTicket(ticket, buffer)
        trade.setAmount(amount, buffer)
        trade.setPrice(price, buffer)
        trade.setBuy(buy, buffer)
        recordsCount += 1
    }

    def dayBalance: Double = {
        var balance: Double = 0
        var i: Int = 0
        while (i < recordsCount) {
            val trade = get(i)
            balance += trade.getAmount(buffer) * trade.getPrice(buffer) * (if (trade.isBuy(buffer)) 1 else -1)
            i += 1
        }
        balance
    }

    private def get(index: Int) = {
        val offset: Int = index * flyweight.getObjectSize
        flyweight.setObjectOffset(offset)
        flyweight
    }

    def destroy = buffer.clear
}

object BufferTrade {
    private var offset: Int = 0
    private final val ticketOffset: Int = {
        offset + 0
    }
    private final val amountOffset: Int = {
        offset += 4
        offset
    }
    private final val priceOffset: Int = {
        offset += 4
        offset
    }
    private final val buyOffset: Int = {
        offset += 4
        offset
    }
    private final val objectSize: Int = {
        offset += 1
        offset
    }
    private var objectOffset: Int = 0

    def getObjectSize: Int = {
        objectSize
    }

    def setObjectOffset(objOffset: Int) = {
        objectOffset = objOffset
    }

    def setTicket(ticket: Int, buffer: ByteBuffer) = {
        buffer.putInt(objectOffset + ticketOffset, ticket)
    }

    def getPrice(buffer: ByteBuffer): Int = {
        buffer.getInt(objectOffset + priceOffset)
    }

    def setPrice(price: Int, buffer: ByteBuffer) = {
        buffer.putInt(objectOffset + priceOffset, price)
    }

    def getAmount(buffer: ByteBuffer): Int = {
        buffer.getInt(objectOffset + amountOffset)
    }

    def setAmount(quantity: Int, buffer: ByteBuffer) = {
        buffer.putInt(objectOffset + amountOffset, quantity)
    }

    def isBuy(buffer: ByteBuffer): Boolean = {
        buffer.get(objectOffset + buyOffset) == 1
    }

    def setBuy(buy: Boolean, buffer: ByteBuffer) = {
        buffer.put(objectOffset + buyOffset, (if (buy) 1 else 0))
    }
}