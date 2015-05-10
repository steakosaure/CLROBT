package clrobots;

import clrobots.interfaces.IAction;
import clrobots.interfaces.IDecision;
import clrobots.interfaces.Iinteragir;

@SuppressWarnings("all")
public abstract class Agir {
  public interface Requires {
    /**
     * This can be called by the implementation to access this required port.
     * 
     */
    public IDecision decision();
    
    /**
     * This can be called by the implementation to access this required port.
     * 
     */
    public Iinteragir interagir();
  }
  
  public interface Component extends Agir.Provides {
  }
  
  public interface Provides {
    /**
     * This can be called to access the provided port.
     * 
     */
    public IAction action();
  }
  
  public interface Parts {
  }
  
  public static class ComponentImpl implements Agir.Component, Agir.Parts {
    private final Agir.Requires bridge;
    
    private final Agir implementation;
    
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
      	throw new RuntimeException("make_action() in clrobots.Agir should not return null.");
      }
    }
    
    protected void initProvidedPorts() {
      init_action();
    }
    
    public ComponentImpl(final Agir implem, final Agir.Requires b, final boolean doInits) {
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
  
  private Agir.ComponentImpl selfComponent;
  
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
  protected Agir.Provides provides() {
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
  protected Agir.Requires requires() {
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
  protected Agir.Parts parts() {
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
  public synchronized Agir.Component _newComponent(final Agir.Requires b, final boolean start) {
    if (this.init) {
    	throw new RuntimeException("This instance of Agir has already been used to create a component, use another one.");
    }
    this.init = true;
    Agir.ComponentImpl  _comp = new Agir.ComponentImpl(this, b, true);
    if (start) {
    	_comp.start();
    }
    return _comp;
  }
}
