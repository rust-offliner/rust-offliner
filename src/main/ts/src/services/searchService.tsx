import { FormEvent, SetStateAction } from 'react'

const handleSubmit = async (
  event: FormEvent<HTMLFormElement>,
  queryValue: string,
  setQueryValue: { (value: SetStateAction<string>): void; (arg0: string): void },
) => {
  event.preventDefault()
  try {
    const url = 'offline.bieda.it/api/query'
    const res = await fetch(url, {
      method: 'POST',
      body: JSON.stringify({
        query: queryValue,
      }),
    })
    if (res.status === 200) {
      setQueryValue('')
    } else {
      console.log('error')
    }
  } catch (err) {
    console.log(err)
  }
}

export default handleSubmit
