package clrobots;

import clrobots.interfaces.IGetKnowledge;
import clrobots.interfaces.ISetKnowledge;

@SuppressWarnings("all")
public abstract class Knowledge {
  public interface Requires {
  }
  
  public interface Component extends Knowledge.Provides {
  }
  
  public interface Provides {
    /**
     * This can be called to access the provided port.
     * 
     */
    public IGetKnowledge getKnowledge();
    
    /**
     * This can be called to access the provided port.
     * 
     */
    public ISetKnowledge setKnowledge();
  }
  
  public interface Parts {
  }
  
  public static class ComponentImpl implements Knowledge.Component, Knowledge.Parts {
    private final Knowledge.Requires bridge;
    
    private final Knowledge implementation;
    
    public void start() {
      this.implementation.start();
      this.implementation.started = true;
    }
    
    protected void initParts() {
      
    }
    
    private void init_getKnowledge() {
      assert this.getKnowledge == null: "This is a bug.";
      this.getKnowledge = this.implementation.make_getKnowledge();
      if (this.getKnowledge == null) {
      	throw new RuntimeException("make_getKnowledge() in clrobots.Knowledge should not return null.");
      }
    }
    
    private void init_setKnowledge() {
      assert this.setKnowledge == null: "This is a bug.";
      this.setKnowledge = this.implementation.make_setKnowledge();
      if (this.setKnowledge == null) {
      	throw new RuntimeException("make_setKnowledge() in clrobots.Knowledge should not return null.");
      }
    }
    
    protected void initProvidedPorts() {
      init_getKnowledge();
      init_setKnowledge();
    }
    
    public ComponentImpl(final Knowledge implem, final Knowledge.Requires b, final boolean doInits) {
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
    
    private IGetKnowledge getKnowledge;
    
    public IGetKnowledge getKnowledge() {
      return this.getKnowledge;
    }
    
    private ISetKnowledge setKnowledge;
    
    public ISetKnowledge setKnowledge() {
      return this.setKnowledge;
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
  
  private Knowledge.ComponentImpl selfComponent;
  
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
  protected Knowledge.Provides provides() {
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
  protected abstract IGetKnowledge make_getKnowledge();
  
  /**
   * This should be overridden by the implementation to define the provided port.
   * This will be called once during the construction of the component to initialize the port.
   * 
   */
  protected abstract ISetKnowledge make_setKnowledge();
  
  /**
   * This can be called by the implementation to access the required ports.
   * 
   */
  protected Knowledge.Requires requires() {
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
  protected Knowledge.Parts parts() {
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
  public synchronized Knowledge.Component _newComponent(final Knowledge.Requires b, final boolean start) {
    if (this.init) {
    	throw new RuntimeException("This instance of Knowledge has already been used to create a component, use another one.");
    }
    this.init = true;
    Knowledge.ComponentImpl  _comp = new Knowledge.ComponentImpl(this, b, true);
    if (start) {
    	_comp.start();
    }
    return _comp;
  }
  
  /**
   * Use to instantiate a component from this implementation.
   * 
   */
  public Knowledge.Component newComponent() {
    return this._newComponent(new Knowledge.Requires() {}, true);
  }
}
