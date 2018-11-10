import { createStore, applyMiddleware } from "redux";
import thunk from "redux-thunk";
import logger from "redux-logger";
import rootReducer from "./reducer";
import { persistStore, persistReducer } from "redux-persist";

import storage from "redux-persist/lib/storage"; // defaults to localStorage for web and AsyncStorage for react-native

// const tempstore = createStore(
//   reducer,
//   window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__()
// );

// const store = createStore(tempstore, {}, applyMiddleware(thunk, logger));

const persistConfig = {
  key: "root",
  storage
};

const persistedReducer = persistReducer(persistConfig, rootReducer);

export default () => {
  let store = createStore(persistedReducer, applyMiddleware(thunk, logger));
  let persistor = persistStore(store);
  return { store, persistor };
};

// const store = createStore(
//   reducer /* preloadedState, */,
//   window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__(),
//   applyMiddleware(thunk, logger)
// );
// export default store;
