package clrobots;

import clrobots.interfaces.Do;

@SuppressWarnings("all")
public abstract class Percevoir<Context, SelfKnowledge> {
  public interface Requires<Context, SelfKnowledge> {
    /**
     * This can be called by the implementation to access this required port.
     * 
     */
    public Context context();
    
    /**
     * This can be called by the implementation to access this required port.
     * 
     */
    public SelfKnowledge knowledge();
    
    /**
     * This can be called by the implementation to access this required port.
     * 
     */
    public Do decision();
  }
  
  public interface Component<Context, SelfKnowledge> extends Percevoir.Provides<Context, SelfKnowledge> {
  }
  
  public interface Provides<Context, SelfKnowledge> {
    /**
     * This can be called to access the provided port.
     * 
     */
    public Do perception();
  }
  
  public interface Parts<Context, SelfKnowledge> {
  }
  
  public static class ComponentImpl<Context, SelfKnowledge> implements Percevoir.Component<Context, SelfKnowledge>, Percevoir.Parts<Context, SelfKnowledge> {
    private final Percevoir.Requires<Context, SelfKnowledge> bridge;
    
    private final Percevoir<Context, SelfKnowledge> implementation;
    
    public void start() {
      this.implementation.start();
      this.implementation.started = true;
    }
    
    protected void initParts() {
      
    }
    
    private void init_perception() {
      assert this.perception == null: "This is a bug.";
      this.perception = this.implementation.make_perception();
      if (this.perception == null) {
      	throw new RuntimeException("make_perception() in clrobots.Percevoir<Context, SelfKnowledge> should not return null.");
      }
    }
    
    protected void initProvidedPorts() {
      init_perception();
    }
    
    public ComponentImpl(final Percevoir<Context, SelfKnowledge> implem, final Percevoir.Requires<Context, SelfKnowledge> b, final boolean doInits) {
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
    
    private Do perception;
    
    public Do perception() {
      return this.perception;
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
  
  private Percevoir.ComponentImpl<Context, SelfKnowledge> selfComponent;
  
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
  protected Percevoir.Provides<Context, SelfKnowledge> provides() {
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
  protected abstract Do make_perception();
  
  /**
   * This can be called by the implementation to access the required ports.
   * 
   */
  protected Percevoir.Requires<Context, SelfKnowledge> requires() {
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
  protected Percevoir.Parts<Context, SelfKnowledge> parts() {
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
  public synchronized Percevoir.Component<Context, SelfKnowledge> _newComponent(final Percevoir.Requires<Context, SelfKnowledge> b, final boolean start) {
    if (this.init) {
    	throw new RuntimeException("This instance of Percevoir has already been used to create a component, use another one.");
    }
    this.init = true;
    Percevoir.ComponentImpl<Context, SelfKnowledge>  _comp = new Percevoir.ComponentImpl<Context, SelfKnowledge>(this, b, true);
    if (start) {
    	_comp.start();
    }
    return _comp;
  }
}
