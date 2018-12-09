import { createStore, applyMiddleware, compose } from "redux";
import thunk from "redux-thunk";
import logger from "redux-logger";
// import rootReducer from "./reducer";
import { WeatherReducer } from "../redux/weatherReducer";
import { NewsReducer } from "../redux/newsReducer";
import { StocksReducer } from "../redux/stocksReducer";
import { UserReducer } from "../redux/reducer";
import { persistStore, persistReducer } from "redux-persist";
import { combineReducers } from "redux";

import storage from "redux-persist/lib/storage"; // defaults to localStorage for web and AsyncStorage for react-native
import { News } from "../News";

// const tempstore = createStore(
//   reducer,
//   window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__()
// );

// const store = createStore(tempstore, {}, applyMiddleware(thunk, logger));

const persistConfig = {
  key: "root",
  storage
};

const composeEnhancers =
  typeof window === "object" && window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__
    ? window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__({
        // Specify extension’s options like name, actionsBlacklist, actionsCreators, serialize...
      })
    : compose;

const enhancer = composeEnhancers(
  applyMiddleware(thunk, logger)
  // other store enhancers if any
);
const rootReducer = combineReducers({
  UserReducer: UserReducer,
  WeatherReducer: WeatherReducer,
  NewsReducer: NewsReducer,
  StocksReducer: StocksReducer
});
const persistedReducer = persistReducer(persistConfig, rootReducer);

export default () => {
  let store = createStore(persistedReducer, enhancer);
  let persistor = persistStore(store);
  return { store, persistor };
};

// const store = createStore(
//   reducer /* preloadedState, */,
//   window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__(),
//   applyMiddleware(thunk, logger)
// );
// export default store;
