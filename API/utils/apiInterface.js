const CoinbasePro = require("coinbase-pro");
const publicClient = new CoinbasePro.PublicClient();


/*
	API interface to fetch live prices and info from Coinbase Pro

	https://github.com/coinbase/coinbase-pro-node

	Coinbase Pro endpoints:
		- https://api.pro.coinbase.com/products/BTC-EUR/ticker
*/



// Simple caching mechanism
var priceCache = {};
const cacheInterval = 3 * 1000;	// Invalidate all prices older than this interval



module.exports = {

	async getPrice(currency, fiat="EUR") {

		// Return cached value if it exist/is recent enough
		if (fiat in priceCache) {
			if (currency in priceCache[fiat] && new Date().getTime() - priceCache[fiat][currency].timestamp < cacheInterval) {
				return priceCache[fiat][currency].price;
			} 
		}

		// Price is not in cache, fetch it from the Coinbase Pro API
		let symbol = (currency.trim() + "-" + fiat.trim()).toUpperCase();	// Build pair id from currency symbol and fiat
		let ticker = await publicClient.getProductTicker(symbol);
		let price  = parseFloat(ticker.price);

		// Save price in cache
		if (!(fiat in priceCache))
			priceCache[fiat] = {};
		priceCache[fiat][currency] = {
			timestamp: new Date().getTime(),
			price: price
		};

		return price;
	}
}
