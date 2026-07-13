export function ResultPanel({ result, httpStatus }) {
  const json = JSON.stringify(result, null, 2)

  return (
    <div className="result-panel">
      <div className="result-head">
        <h2>Raw result</h2>
        <span className="http-pill">HTTP {httpStatus}</span>
      </div>
      <pre>{json}</pre>
    </div>
  )
}
