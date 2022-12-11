import { useState } from "react"
import handleSubmit from "../../../services/searchService"
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"
import { faMagnifyingGlass } from "@fortawesome/free-solid-svg-icons"
import styles from "./search.module.scss"

export default function Search() {
  const [searchValue, setSearchValue] = useState("")
  return (
    <form
      onSubmit={(event) => {
        handleSubmit(event, searchValue, setSearchValue)
      }}
    >
      <div className={styles.searchFormArea}>
        <input
          type={"text"}
          aria-label={"Search for a Rust server"}
          value={searchValue}
          onChange={(e) => setSearchValue(e.target.value)}
          placeholder={"Rust servers"}
        />
        <button type='submit' title='Search for a server'>
          <FontAwesomeIcon icon={faMagnifyingGlass} />
        </button>
      </div>
    </form>
  )
}
