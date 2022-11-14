import { useState } from 'react'

export default function Search() {
  const [searchValue, setSearchValue] = useState('')

  // todo search for data from api
  return (
    <form method='GET'>
      <input
        type={'text'}
        aria-label={'Search for a Rust server'}
        value={searchValue}
        onChange={(e) => setSearchValue(e.target.value)}
        placeholder={'Rust servers'}
      />
      <input type='submit' value='Search' />
    </form>
  )
}
