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
      <div className={'search-form-area'}>
        <input
          type={'text'}
          className={'search-form-area__input'}
          aria-label={'Search for a Rust server'}
          value={searchValue}
          onChange={(e) => setSearchValue(e.target.value)}
          placeholder={'Rust servers'}
        />
        <button type='submit' className={'search-form-area__submit-button'}>
          <FontAwesomeIcon icon={faMagnifyingGlass} />
        </button>
      </div>
    </form>
  )
}
