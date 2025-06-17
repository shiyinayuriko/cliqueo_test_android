## Core Features

- [x] A. Real-Time (or Mocked) Market Data Feed
  - [x] Fetch (or simulate) live price data for *at least* one crypto pair (e.g., BTC-USDT).
  - [x] Display continuously updating bid/ask and last trade on a *Market Overview* screen.
  - [x] Updates should occur ≤ 500 ms without jank on a Pixel 6 (≈ 3 000 msgs/s sustained in mock mode is a plus).
  
- [x] B. Candlestick (or Line) Chart
  - [x] Embed the chosen chart component inside a *Detail* screen.
  - [x] Render price history as candlesticks (preferred) or a line chart.
  - [x] Support common interactions: pinch-zoom, pan/scroll, and value marker on tap (O/H/L/C display).

- [ ] C. Basic Trading Interface
  - [ ] Watchlist Screen – Add/remove crypto pairs; persist locally (e.g., DataStore).
  - [ ] Order Entry Sheet – For the selected pair, allow:
    - `Price`, `Amount`, `Side` (Buy/Sell), `Order Type` (Market/Limit).
    - “Confirm” → store the order locally (mock) and reflect in state.
  - [ ] Order History / Open Orders – List of placed orders with status (Filled, Canceled, etc.).

- [ ] D. Secure Key / Settings Management \*(Optional)\*
  - Demonstrate storing a mock API Key / Secret using Android Keystore or EncryptedSharedPreferences.

------

## Extended / Optional Enhancements

- [x] WebSocket Integration with a public API (Binance, Coinbase Pro, etc.).
- [ ] Jetpack Compose + StateFlow end-to-end (no XML).
- [ ] Multiple Timeframes & Indicators (MA, RSI overlays).
- [ ] Push Notifications / Local Alerts when price crosses a threshold (WorkManager or FCM).
- [ ] Biometric Prompt before confirming an order, binding a nonce to fingerprint/face-auth.

Feel free to note unfinished stretch items in the README—clarity about next steps is valued. Good luck, and happy coding!

------

## Custom Tasks

- [x] check Material 3 guidance 
- [ ] (Optional) responsive design for tablets
- [ ] check `Choreographer` or `JankStats`
- [ ] setup unit tests (e.g., price-parsing logic, order-placement flow).
- [ ] (Optional) One instrumentation/UI test with Espresso or Compose UI Test.
- [ ] Explain any performance optimizations in `README`.
- [ ] instructions info in README 
- [ ] optimize api retry strategy
- [ ] http3 + retrofit log and intercept
- [ ] add loading status view for ws&http
- [ ] add cache for network fetch
- [ ] ~~support pull refresh~~
- [ ] replace LiveData to StateFlow
- [ ] optimize the loading in favourite page with separate api request?
- [ ] polish UI tab height