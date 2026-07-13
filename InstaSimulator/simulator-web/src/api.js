export async function runBindingScenario() {
  const response = await fetch('/scenarios/binding')
  const data = await response.json().catch(() => null)

  if (!data) {
    throw new Error(`Empty response (HTTP ${response.status})`)
  }

  return {
    ok: response.ok && data.success === true,
    httpStatus: response.status,
    result: data,
  }
}
