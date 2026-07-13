import { useState } from 'react'
import { runBindingScenario } from './api'
import { ScenarioFlow } from './components/ScenarioFlow'
import { StepList } from './components/StepList'
import { ResultPanel } from './components/ResultPanel'

const STEPS = [
  { id: 'health-check', label: 'Health Check', hint: 'Local readiness probe' },
  { id: 'bind1', label: 'Bind1', hint: 'Device bind on staging' },
  { id: 'register1', label: 'Register1', hint: 'Customer register / DEVCHNG' },
]

export default function App() {
  const [running, setRunning] = useState(false)
  const [error, setError] = useState(null)
  const [payload, setPayload] = useState(null)

  async function onRun() {
    setRunning(true)
    setError(null)
    setPayload(null)
    try {
      const response = await runBindingScenario()
      setPayload(response)
    } catch (err) {
      setError(err instanceof Error ? err.message : String(err))
    } finally {
      setRunning(false)
    }
  }

  const result = payload?.result
  const overallStatus = result?.status ?? (error ? 'ERROR' : null)
  const failed = overallStatus === 'FAILURE' || overallStatus === 'ERROR' || payload?.ok === false

  return (
    <div className="page">
      <div className="atmosphere" aria-hidden="true" />
      <div className="grid-noise" aria-hidden="true" />

      <header className="topbar">
        <div className="brand-lockup">
          <span className="brand-mark" aria-hidden="true" />
          <div>
            <p className="brand-name">InstaSimulator</p>
            <p className="brand-sub">Mobile API scenario runner</p>
          </div>
        </div>
        <p className="env-chip">staging · live calls</p>
      </header>

      <main className="stage">
        <section className="hero">
          <h1 className="hero-title">
            Binding
            <span> Scenario</span>
          </h1>
          <p className="hero-copy">
            Run Health Check → Bind1 → Register1 against InstaPay staging.
            The scenario fails if any step returns a non-success business code.
          </p>

          <div className="cta-row">
            <button
              type="button"
              className="run-btn"
              onClick={onRun}
              disabled={running}
            >
              <span className={running ? 'run-dot pulse' : 'run-dot'} />
              {running ? 'Running scenario…' : 'Run Binding Scenario'}
            </button>

            {overallStatus && (
              <div
                className={`overall-badge ${failed ? 'is-fail' : 'is-pass'}`}
                role="status"
              >
                <span className="overall-label">Overall</span>
                <strong>{overallStatus}</strong>
                {typeof result?.durationMs === 'number' && (
                  <span className="overall-meta">{result.durationMs} ms</span>
                )}
              </div>
            )}
          </div>
        </section>

        <ScenarioFlow
          steps={STEPS}
          resultSteps={result?.steps}
          running={running}
        />

        {error && (
          <p className="error-banner" role="alert">
            {error}
          </p>
        )}

        {result && (
          <section className="results">
            <StepList steps={result.steps} />
            <ResultPanel result={result} httpStatus={payload?.httpStatus} />
          </section>
        )}
      </main>

      <footer className="foot">
        <span>Only code 00000 counts as InstaPay success</span>
        <span>HTTP 422 when scenario fails</span>
      </footer>
    </div>
  )
}
