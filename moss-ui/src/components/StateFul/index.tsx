import React from 'react'

export const enum ViewState {
  IDLE = 'IDLE',
  LOADING = 'LOADING',
  RENDER = 'RENDER',
  EMPTY = 'EMPTY',
  ERROR = 'ERROR',
}

export type SetViewStateFunctionType = (vs: ViewState | any) => void

export type StateFulRenderPropsType = {
  viewState: ViewState
  setViewState: SetViewStateFunctionType
}

interface IStateFulProps<T> {
  viewState?: ViewState | T
}

interface IStateFulState<T> {
  viewState?: ViewState | T
}

class Stateful<T> extends React.Component<IStateFulProps<T>, IStateFulState<T>> {
  public state: IStateFulState<T> = {
    viewState: ViewState.IDLE,
  }

  private setViewState = (viewState: T) => {
    this.setState({
      viewState,
    })
  }

  render() {
    const { viewState: vs } = this.state
    const { viewState, children } = this.props

    return (
      (children &&
        typeof children === 'function' &&
        children({ viewState: viewState || vs, setViewState: this.setViewState })) ||
      null
    )
  }
}

export default Stateful
