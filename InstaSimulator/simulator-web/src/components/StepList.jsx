export function StepList({ steps }) {
  if (!steps?.length) return null

  return (
    <div className="step-list">
      <h2>Step outcomes</h2>
      <ul>
        {steps.map((step) => (
          <li key={step.stepName} className={step.success ? 'ok' : 'bad'}>
            <div className="step-head">
              <span className="step-name">{step.stepName}</span>
              <span className="step-flag">{step.status}</span>
            </div>
            <p className="step-detail">
              <code>{step.statusCode}</code>
              <span>{step.message}</span>
            </p>
          </li>
        ))}
      </ul>
    </div>
  )
}
