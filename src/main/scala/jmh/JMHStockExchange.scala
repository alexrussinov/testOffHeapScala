package jmh

import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import testOffheap._

/**
 * Created by Aleksey Voronets on 26.08.15.
 */
class JMHStockExchange {

    @Benchmark
    @BenchmarkMode(Array(Mode.AverageTime))
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    def arrayInt: Double = {
        val exchange: StockExchange = new ArrayIntStockExchange()
        var i: Int = 0
        while (i < StockExchange.TRADES_PER_DAY) {
            exchange.order(i, i, i, (i & 1) == 0)
            i += 1
        }
        println("Day balance: " + exchange.dayBalance)
        exchange.dayBalance
    }

    @Benchmark
    @BenchmarkMode(Array(Mode.AverageTime))
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    def listBufferTrade: Double = {
        val exchange: StockExchange = new ListBufferStockExchange()
        var i: Int = 0
        while (i < StockExchange.TRADES_PER_DAY) {
            exchange.order(i, i, i, (i & 1) == 0)
            i += 1
        }
        println("Day balance: " + exchange.dayBalance)
        exchange.dayBalance
    }


    @Benchmark
    @BenchmarkMode(Array(Mode.AverageTime))
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    def vectorInt: Double = {
        val exchange: StockExchange = new VectorTradeStockExchange()
        var i: Int = 0
        while (i < StockExchange.TRADES_PER_DAY) {
            exchange.order(i, i, i, (i & 1) == 0)
            i += 1
        }
        println("Day balance: " + exchange.dayBalance)
        exchange.dayBalance
    }

    @Benchmark
    @BenchmarkMode(Array(Mode.AverageTime))
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    def byteBufferDirectInt: Double = {
        val exchange: StockExchange = new ByteBufferStockExchange(true)
        var i: Int = 0
        while (i < StockExchange.TRADES_PER_DAY) {
            exchange.order(i, i, i, (i & 1) == 0)
            i += 1
        }
        println("Day balance: " + exchange.dayBalance)
        exchange.dayBalance
    }

    @Benchmark
    @BenchmarkMode(Array(Mode.AverageTime))
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    def arrayList: Double = {
        val exchange: StockExchange = new ArrayListStockExchange
        var i: Int = 0
        while (i < StockExchange.TRADES_PER_DAY) {
            exchange.order(i, i, i, (i & 1) == 0)
            i += 1
        }
        println("Day balance: " + exchange.dayBalance)
        exchange.dayBalance
    }

    @Benchmark
    @BenchmarkMode(Array(Mode.AverageTime))
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    def unsafe() {
        val exchange = new UnsafeStockExchange()
        var i = 0
        while(i < StockExchange.TRADES_PER_DAY) {
            exchange.order(i, i, i, (i & 1) == 0)
            i += 1
        }
        exchange.dayBalance()
        exchange.destroy()
    }

    @Benchmark
    @BenchmarkMode(Array(Mode.AverageTime))
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    def offHeapScala: Double = {
        val exchange: StockExchange = new OffHeapScalaStockExchange
        var i: Int = 0
        while (i < StockExchange.TRADES_PER_DAY) {
            exchange.order(i, i, i, (i & 1) == 0)
            i += 1
        }
        exchange.dayBalance
    }

    @Benchmark
    @BenchmarkMode(Array(Mode.AverageTime))
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    def offHeapRegionScala: Double = {
        val exchange: StockExchange = new OffHeapScalaStockExchangeRegion
        var i: Int = 0
        while (i < StockExchange.TRADES_PER_DAY) {
            exchange.order(i, i, i, (i & 1) == 0)
            i += 1
        }
        exchange.dayBalance
    }
}

