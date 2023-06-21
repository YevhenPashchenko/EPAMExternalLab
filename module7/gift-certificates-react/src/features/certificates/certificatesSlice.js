import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";

export const fetchCertificates = createAsyncThunk('/certificates',
    async (...args) => {
      const searchParams = args[0][0];
      const token = args[0][1];

      function checkSort() {
        if (searchParams["title-sort-direction"] !== null) {
          return {"name": searchParams["title-sort-direction"]};
        } else {
          return {"lastUpdateDate": searchParams["date-sort-direction"]}
        }
      }

      const params = {
        "tag-name": searchParams["tag-name"],
        "part-name": searchParams.title,
        "part-description": searchParams.title,
        "sort-by": checkSort()
      }
      return fetch(
          `http://localhost:8000/gift-certificates/sort?page=${searchParams.page
          - 1}&size=${searchParams.size}`, {
            method: "POST",
            headers: {
              Authorization: "Bearer " + token.access_token,
              "Content-Type": "application/json"
            },
            body: JSON.stringify(params)
          }).then(res => res.json());
    });

export const certificatesSlice = createSlice({
  name: 'certificates',
  initialState: {
    certificates: [],
    page: {},
    message: ''
  },
  reducers: {},
  extraReducers(builder) {
    builder.addCase(fetchCertificates.fulfilled, (state, action) => {
      if (action.payload.message !== undefined) {
        state.message = action.payload.message;
        state.certificates = {};
        state.page = {};
        return state;
      }
      const certificatesList = action.payload._embedded;
      if (certificatesList !== undefined) {
        state.certificates = certificatesList.giftCertificateDtoList;
      } else {
        state.certificates = {};
      }
      state.page = action.payload.page;
      return state;
    })
  }
});

export default certificatesSlice.reducer;