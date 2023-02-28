import { LatLngBoundsExpression } from 'leaflet'
import { useMap } from 'react-leaflet'
import { BiZoomIn, BiZoomOut, BiReset } from 'react-icons/bi'
import styles from './CustomZoomControl.module.scss'

export default function CustomZoomControl(props: {
  bounds: LatLngBoundsExpression
}) {
  const map = useMap()

  return (
    <section className={styles.zoomControl}>
      <button
        type='button'
        aria-label='Zoom in'
        title='Zoom in'
        onClick={() => map.zoomIn()}
      >
        <BiZoomIn />
      </button>
      <button
        type='button'
        aria-label='Zoom out'
        title='Zoom out'
        onClick={() => map.zoomOut()}
      >
        <BiZoomOut />
      </button>
      <button
        type='button'
        aria-label='Reset zoom and center'
        title='Reset zoom and center'
        onClick={() => map.fitBounds(props.bounds)}
      >
        <BiReset />
      </button>
    </section>
  )
}
