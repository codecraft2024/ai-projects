export function ScenarioFlow({ steps, resultSteps, running }) {
  return (
    <ol className="flow" aria-label="Scenario steps">
      {steps.map((step, index) => {
        const match = resultSteps?.find((s) => s.stepName === step.id)
        const state = running
          ? 'running'
          : match
            ? match.success
              ? 'pass'
              : 'fail'
            : 'idle'

        return (
          <li key={step.id} className={`flow-step state-${state}`} style={{ '--i': index }}>
            <div className="flow-index">{String(index + 1).padStart(2, '0')}</div>
            <div className="flow-body">
              <p className="flow-label">{step.label}</p>
              <p className="flow-hint">{step.hint}</p>
              {match && (
                <p className="flow-code">
                  {match.statusCode}
                  {match.message ? ` · ${match.message}` : ''}
                </p>
              )}
            </div>
            <div className={`flow-status status-${state}`}>
              {state === 'running' && '…'}
              {state === 'pass' && 'PASS'}
              {state === 'fail' && 'FAIL'}
              {state === 'idle' && 'WAIT'}
            </div>
            {index < steps.length - 1 && <div className="flow-connector" aria-hidden="true" />}
          </li>
        )
      })}
    </ol>
  )
}
