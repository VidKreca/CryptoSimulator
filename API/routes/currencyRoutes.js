const express = require('express');
var router = express.Router();
const apiInterface = require("../utils/apiInterface");
const options = require("../options.json");

/*
	Base URL: /api/*
*/


// API config
const validFiatCurrencies   = options.validFiatCurrencies;
const validCryptoCurrencies = options.validCryptoCurrencies;



// Get all crypto currencies
router.get("/list/:fiat?", async (req, res) => {

	// Set default FIAT currency to EUR
	if (!req.params.fiat)
		req.params.fiat = "EUR";
	// Check if provided FIAT currency is valid
	if (!validFiatCurrencies.includes(req.params.fiat))
		return res.status(400).json({
			success: false,
			message: "Invalid FIAT currency symbol. Valid: " + validFiatCurrencies
		});

	// Fetch all product prices
	let currencies = []
	for (let i = 0; i < validCryptoCurrencies.length; i++) {
		let symbol = validCryptoCurrencies[i].symbol;
		let price = await apiInterface.getPrice(symbol, req.params.fiat);
		currencies.push({
			symbol: symbol, 
			name: validCryptoCurrencies[i].name,
			price: price
		});
	}

	// Build data object to return
	let data = {
		success: true,
		timestamp: (new Date()).getTime(),		// Milliseconds since 1970
		fiat: req.params.fiat,
		currencies: currencies
	};


	return res.status(200).json(data);
});







// Get single crypto currency
router.get("/:symbol/:fiat?", async (req, res) => {

	// Set default FIAT currency to EUR
	if (!req.params.fiat)
		req.params.fiat = "EUR";
	// Check if provided FIAT currency is valid
	if (!validFiatCurrencies.includes(req.params.fiat))
		return res.status(400).json({
			success: false,
			message: "Invalid FIAT currency symbol. Valid: " + validFiatCurrencies
		});


	// Check if provided symbol is valid
	let symbol = req.params.symbol.toUpperCase();
	if (!validCryptoCurrencies.map(x => x.symbol).includes(symbol))
		return res.status(400).json({
			success: false,
			message: "Invalid crypto currency symbol. Valid: " + validCryptoCurrencies.map(x => x.symbol)
		});


	// Find name from symbol
	let index = validCryptoCurrencies.findIndex(x => x.symbol == symbol);
	let name = "Invalid";
	if (index != -1)
		name = validCryptoCurrencies[index].name;


	// Build data object to return
	let stats = await apiInterface.getStats(symbol, req.params.fiat);

	let data = {
		success: true,
		timestamp: (new Date()).getTime(),		// Milliseconds since 1970
		fiat: req.params.fiat,
		symbol: symbol,
		name: name
	};

	data = {...data, ...stats};

	return res.status(200).json(data);
});



module.exports = router;
