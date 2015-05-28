package clrobots;

import clrobots.interfaces.IAction;
import clrobots.interfaces.Iinteragir;

@SuppressWarnings("all")
public abstract class Action<Runnable> {
  public interface Requires<Runnable> {
    /**
     * This can be called by the implementation to access this required port.
     * 
     */
    public Iinteragir interagir();
  }
  
  public interface Component<Runnable> extends Action.Provides<Runnable> {
  }
  
  public interface Provides<Runnable> {
    /**
     * This can be called to access the provided port.
     * 
     */
    public IAction action();
  }
  
  public interface Parts<Runnable> {
  }
  
  public static class ComponentImpl<Runnable> implements Action.Component<Runnable>, Action.Parts<Runnable> {
    private final Action.Requires<Runnable> bridge;
    
    private final Action<Runnable> implementation;
    
    public void start() {
      this.implementation.start();
      this.implementation.started = true;
    }
    
    protected void initParts() {
      
    }
    
    private void init_action() {
      assert this.action == null: "This is a bug.";
      this.action = this.implementation.make_action();
      if (this.action == null) {
      	throw new RuntimeException("make_action() in clrobots.Action<Runnable> should not return null.");
      }
    }
    
    protected void initProvidedPorts() {
      init_action();
    }
    
    public ComponentImpl(final Action<Runnable> implem, final Action.Requires<Runnable> b, final boolean doInits) {
      this.bridge = b;
      this.implementation = implem;
      
      assert implem.selfComponent == null: "This is a bug.";
      implem.selfComponent = this;
      
      // prevent them to be called twice if we are in
      // a specialized component: only the last of the
      // hierarchy will call them after everything is initialised
      if (doInits) {
      	initParts();
      	initProvidedPorts();
      }
    }
    
    private IAction action;
    
    public IAction action() {
      return this.action;
    }
  }
  
  /**
   * Used to check that two components are not created from the same implementation,
   * that the component has been started to call requires(), provides() and parts()
   * and that the component is not started by hand.
   * 
   */
  private boolean init = false;;
  
  /**
   * Used to check that the component is not started by hand.
   * 
   */
  private boolean started = false;;
  
  private Action.ComponentImpl<Runnable> selfComponent;
  
  /**
   * Can be overridden by the implementation.
   * It will be called automatically after the component has been instantiated.
   * 
   */
  protected void start() {
    if (!this.init || this.started) {
    	throw new RuntimeException("start() should not be called by hand: to create a new component, use newComponent().");
    }
  }
  
  /**
   * This can be called by the implementation to access the provided ports.
   * 
   */
  protected Action.Provides<Runnable> provides() {
    assert this.selfComponent != null: "This is a bug.";
    if (!this.init) {
    	throw new RuntimeException("provides() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if provides() is needed to initialise the component.");
    }
    return this.selfComponent;
  }
  
  /**
   * This should be overridden by the implementation to define the provided port.
   * This will be called once during the construction of the component to initialize the port.
   * 
   */
  protected abstract IAction make_action();
  
  /**
   * This can be called by the implementation to access the required ports.
   * 
   */
  protected Action.Requires<Runnable> requires() {
    assert this.selfComponent != null: "This is a bug.";
    if (!this.init) {
    	throw new RuntimeException("requires() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if requires() is needed to initialise the component.");
    }
    return this.selfComponent.bridge;
  }
  
  /**
   * This can be called by the implementation to access the parts and their provided ports.
   * 
   */
  protected Action.Parts<Runnable> parts() {
    assert this.selfComponent != null: "This is a bug.";
    if (!this.init) {
    	throw new RuntimeException("parts() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if parts() is needed to initialise the component.");
    }
    return this.selfComponent;
  }
  
  /**
   * Not meant to be used to manually instantiate components (except for testing).
   * 
   */
  public synchronized Action.Component<Runnable> _newComponent(final Action.Requires<Runnable> b, final boolean start) {
    if (this.init) {
    	throw new RuntimeException("This instance of Action has already been used to create a component, use another one.");
    }
    this.init = true;
    Action.ComponentImpl<Runnable>  _comp = new Action.ComponentImpl<Runnable>(this, b, true);
    if (start) {
    	_comp.start();
    }
    return _comp;
  }
}
