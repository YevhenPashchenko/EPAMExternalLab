import React, {useContext, useEffect, useState} from 'react';
import {
  InputGroup,
  UncontrolledAlert,
  Input, Button, Table, Form
} from "reactstrap";
import {useSelector} from "react-redux";
import store from "../app/store";
import {fetchCertificates} from "../features/certificates/certificatesSlice";
import ResponsivePagination from "react-responsive-pagination";
import {useNavigate, useSearchParams} from "react-router-dom";
import EditModal from "./EditModal";
import DeleteModal from "./DeleteModal";
import {AuthContext} from "../App";

const CertificateList = () => {
  const {token} = useContext(AuthContext);
  const navigate = useNavigate();
  const certificates = useSelector(state => state.certificates.certificates);
  const page = useSelector(state => state.certificates.page);
  const message = useSelector(state => state.certificates.message);
  const [searchParams] = useSearchParams();
  const [currentPage, setCurrentPage] = useState(1);
  const [currentSize, setCurrentSize] = useState(10);
  const sizeOptions = [10, 20, 50];
  const [tag, setTag] = useState(null);
  const [title, setTitle] = useState(null);
  const [currentDateSort, setCurrentDateSort] = useState(
      {direction: "desc", iconClass: "bi-caret-down-fill"});
  const [currentTitleSort, setCurrentTitleSort] = useState(
      {direction: null, iconClass: "bi-caret-down-fill"});

  useEffect(() => {
    const searchParameters = {
      page: currentPage,
      size: currentSize,
      "tag-name": tag,
      title: title,
      "date-sort-direction": currentDateSort.direction,
      "title-sort-direction": currentTitleSort.direction
    };
    store.dispatch(fetchCertificates([searchParameters, token]));
  }, [currentDateSort.direction, currentPage, currentSize,
    currentTitleSort.direction, tag, title]);

  useEffect(() => {
    const searchParams = {
      page: currentPage,
      size: currentSize,
      "tag-name": tag,
      title: title,
      "date-sort-direction": currentDateSort.direction,
      "title-sort-direction": currentTitleSort.direction
    };
    const filteredSearchParams = Object.fromEntries(
        Object.entries(searchParams).filter(([, value]) => value !== null)
    );
    navigate(`?${new URLSearchParams(filteredSearchParams)}`);
  }, [currentDateSort.direction, currentPage, currentSize,
    currentTitleSort.direction, navigate, tag, title])

  useEffect(() => {
    if (searchParams.get("page") !== null && Number(searchParams.get("page"))
        !== currentPage) {
      setCurrentPage(Number(searchParams.get("page")));
    }
    if (searchParams.get("size") !== null && Number(searchParams.get("size"))
        !== currentSize) {
      setCurrentSize(Number(searchParams.get("size")));
    }
    if (searchParams.get("tag-name") !== tag || searchParams.get("title")
        !== title) {
      let value = "";
      const input = document.querySelector("input[placeholder='Search']");
      if (searchParams.get("tag-name") !== null) {
        value = "#(" + searchParams.get("tag-name") + ") ";
      }
      if (searchParams.get("title") !== null) {
        value += searchParams.get("title");
      }
      input.value = value;
      input.nextElementSibling.click();
    }
    if (searchParams.get("date-sort-direction") !== currentDateSort.direction
        || searchParams.get("title-sort-direction")
        !== currentTitleSort.direction) {
      const desc = {direction: "desc", iconClass: "bi-caret-down-fill"};
      const asc = {direction: "asc", iconClass: "bi-caret-up-fill"};
      if (searchParams.get("date-sort-direction") !== null) {
        if (searchParams.get("date-sort-direction") === "desc") {
          setCurrentDateSort(desc);
        } else {
          setCurrentDateSort(asc);
        }
        setCurrentTitleSort({...currentTitleSort, direction: null});
      }
      if (searchParams.get("title-sort-direction") !== null) {
        if (searchParams.get("title-sort-direction") === "desc") {
          setCurrentTitleSort(desc);
        } else {
          setCurrentTitleSort(asc);
        }
      }
    }
  }, [searchParams])

  function switchSortDirection(currentSortFunction, newSortFunction,
      currentSort, newSort) {
    currentSortFunction({direction: null, iconClass: currentSort.iconClass});
    if (newSort.iconClass === "bi-caret-up-fill") {
      newSortFunction({direction: "desc", iconClass: "bi-caret-down-fill"});
    } else {
      newSortFunction({direction: "asc", iconClass: "bi-caret-up-fill"})
    }
  }

  function isError() {
    if (message.length > 0) {
      return <UncontrolledAlert color="danger"
                                className="text-center">{message}</UncontrolledAlert>
    }
  }

  function isCertificatesExist() {
    if (certificates.length > 0) {
      return <tbody>
      {certificates.map(certificate => {
        return <tr key={certificate.name}>
          <td>{new Date(certificate.lastUpdateDate).toLocaleString(
              "uk-UA")}</td>
          <td>{certificate.name}</td>
          <td>{certificate.tags.map(tag => tag.name).join(" ")}</td>
          <td>{certificate.description}</td>
          <td>{certificate.price}</td>
          <td className="d-flex">
            <Button color="primary">View</Button>
            <EditModal certificate={certificate}/>
            <DeleteModal name={certificate.name}/>
          </td>
        </tr>
      })}
      </tbody>
    }
  }

  function isPageExist() {
    if (page.totalPages !== undefined) {
      return <div className="d-flex col-11 justify-content-center">
        <ResponsivePagination current={currentPage} total={page.totalPages}
                              onPageChange={setCurrentPage}
                              maxWidth={1500}/>
      </div>
    }
  }

  function setSearchParameters(e) {
    const searchText = e.currentTarget.previousElementSibling.value;
    setCurrentPage(1);
    setCurrentDateSort({direction: "desc", iconClass: "bi-caret-down-fill"});
    setCurrentTitleSort({direction: null, iconClass: "bi-caret-down-fill"});
    if (searchText === '') {
      setTag(null);
      setTitle(null);
    }
    if (searchText.indexOf("#") !== -1) {
      let textBeforeTag = searchText.substring(0,
          searchText.indexOf("#")).trim();
      setTag(searchText.substring((searchText.indexOf("#") + 2),
          searchText.indexOf(")", searchText.indexOf("#"))));
      let textAfterTag = '';
      if (searchText.indexOf(")", searchText.indexOf("#")) < searchText.length
          - 1) {
        textAfterTag = searchText.substring(
            (searchText.indexOf(")", searchText.indexOf("#"))) + 1).trim();
      }
      if (textBeforeTag.length === 0 && textAfterTag.length === 0) {
        setTitle(null);
      } else {
        setTitle(textBeforeTag.length !== 0 ? textBeforeTag : textAfterTag);
      }
    } else {
      setTag(null);
      setTitle(searchText);
    }
  }

  return (
      <div className="my-3 p-5 min-vh-100 bg-secondary bg-opacity-50">
        {isError()}
        <InputGroup>
          <Input type="text" placeholder="Search"/>
          <Button onClick={(e) => setSearchParameters(e)} color="primary"
                  className="w-25">Go!</Button>
        </InputGroup>
        <Table bordered className="mt-3 border-black">
          <thead>
          <tr className="table-secondary">
            <th><i onClick={() => switchSortDirection(setCurrentTitleSort,
                setCurrentDateSort, currentTitleSort, currentDateSort)}
                   className={`bi ${currentDateSort.iconClass}`} role="button"/>Datetime
            </th>
            <th><i onClick={() => switchSortDirection(setCurrentDateSort,
                setCurrentTitleSort, currentDateSort, currentTitleSort)}
                   className={`bi ${currentTitleSort.iconClass}`}
                   role="button"/>Title
            </th>
            <th>Tags</th>
            <th>Description</th>
            <th>Price</th>
            <th>Actions</th>
          </tr>
          </thead>
          {isCertificatesExist()}
        </Table>
        <div className="d-flex w-100 me-5 mb-5 justify-content-end">
          {isPageExist()}
          <Form>
            <Input onChange={e => setCurrentSize(Number(e.currentTarget.value))}
                   type="select" value={currentSize}>
              {
                sizeOptions.map((size) => {
                  return <option key={size}>{size}</option>
                })
              }
            </Input>
          </Form>
        </div>
      </div>
  );
};

export default CertificateList;