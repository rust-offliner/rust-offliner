import { useState } from 'react'
import handleSubmit from '../../services/searchService'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faMagnifyingGlass } from '@fortawesome/free-solid-svg-icons'

export default function Search() {
  const [searchValue, setSearchValue] = useState('')
  return (
    <form
      onSubmit={(event) => {
        handleSubmit(event, searchValue, setSearchValue)
      }}
    >
      <input
        type={'text'}
        aria-label={'Search for a Rust server'}
        value={searchValue}
        onChange={(e) => setSearchValue(e.target.value)}
        placeholder={'Rust servers'}
      />
      <input type='submit' value='Search' />
      <FontAwesomeIcon icon={faMagnifyingGlass} />
    </form>
  )
}
