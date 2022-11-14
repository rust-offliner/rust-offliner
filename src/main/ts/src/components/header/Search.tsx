import { useState } from 'react'
import handleSubmit from '../../services/searchService'

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
        onChange={(event) => setSearchValue(event.target.value)}
        placeholder={'Rust servers'}
      />
      <input type='submit' value='Search' />
    </form>
  )
}
