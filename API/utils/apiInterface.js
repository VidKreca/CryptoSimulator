const CoinbasePro = require("coinbase-pro");
const publicClient = new CoinbasePro.PublicClient();


/*
	API interface to fetch live prices and info from Coinbase Pro

	https://github.com/coinbase/coinbase-pro-node

	Coinbase Pro endpoints:
		- https://api.pro.coinbase.com/products/BTC-EUR/ticker
*/



// Simple caching mechanism
var cache = {};
const cacheInterval = 4 * 1000;	// Invalidate all prices older than this interval
const candleCacheInterval = 10 * 1000;



module.exports = {

	async getPrice(currency, fiat="EUR") {

		// Return cached value if it exist/is recent enough
		if (fiat in cache) {
			if (currency in cache[fiat] && new Date().getTime() - cache[fiat][currency].timestamp < cacheInterval) {
				return cache[fiat][currency].price;
			}
		}

		// Price is not in cache, fetch it from the Coinbase Pro API
		let symbol = (currency.trim() + "-" + fiat.trim()).toUpperCase();	// Build pair id from currency symbol and fiat
		let ticker = await publicClient.getProductTicker(symbol);
		let price  = parseFloat(ticker.price);

		// Update price in cache
		if (fiat in cache && currency in cache[fiat])
			cache[fiat][currency].price = price

		return price;
	},




	async getStats(currency, fiat="EUR") {

		// Return cached value if it exist/is recent enough
		if (fiat in cache) {
			if (currency in cache[fiat] && new Date().getTime() - cache[fiat][currency].timestamp < cacheInterval) {
				return cache[fiat][currency];
			}
		}

		// Price is not in cache, fetch it from the Coinbase Pro API
		let symbol = (currency.trim() + "-" + fiat.trim()).toUpperCase();	// Build pair id from currency symbol and fiat
		let stats = await publicClient.getProduct24HrStats(symbol);

		// Change daily change percentage
		let change = (parseFloat(stats.last) - parseFloat(stats.open));
		let change_percentage = (change / parseFloat(stats.open)) * 100;

		stats = {
			price:  parseFloat(stats.last), 
			high:   parseFloat(stats.open),
			low:    parseFloat(stats.low),
			volume: parseFloat(stats.volume),
			volume_30d: parseFloat(stats.volume_30day),
			change: change,
			change_percentage: change_percentage
		}

		// Cache stats
		if (!(fiat in cache))
			cache[fiat] = {};
		stats.timestamp = new Date().getTime();
		cache[fiat][currency] = stats;

		return stats;
	},




	async getCandles(currency, fiat="EUR") {

		let granularity = 3600;
		let dayOffset = 7;
		let from = new Date();
 		from.setDate(from.getDate() - dayOffset);
 		let to = new Date();

		let symbol = (currency.trim() + "-" + fiat.trim()).toUpperCase();	// Build pair id from currency symbol and fiat
		let candles = await publicClient.getProductHistoricRates(symbol, {
			granularity: granularity,
			start: from.toISOString(),
			end: to
		});

		return candles;		

	}
}
