import {configureStore} from "@reduxjs/toolkit";
import certificatesReducer from '../features/certificates/certificatesSlice'

export default configureStore({
  reducer: {
    certificates: certificatesReducer
  },
})