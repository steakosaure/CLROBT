package clrobots;

import clrobots.interfaces.Iinteragir;
import environnement.interfaces.IEnvInfos;
import environnement.interfaces.IEnvInit;
import ui.interfaces.IUpdateUi;

@SuppressWarnings("all")
public abstract class Environnement {
  public interface Requires {
    /**
     * This can be called by the implementation to access this required port.
     * 
     */
    public IUpdateUi updateUi();
  }
  
  public interface Component extends Environnement.Provides {
  }
  
  public interface Provides {
    /**
     * This can be called to access the provided port.
     * 
     */
    public Iinteragir interagir();
    
    /**
     * This can be called to access the provided port.
     * 
     */
    public IEnvInfos envInfos();
    
    /**
     * This can be called to access the provided port.
     * 
     */
    public IEnvInit envInit();
  }
  
  public interface Parts {
  }
  
  public static class ComponentImpl implements Environnement.Component, Environnement.Parts {
    private final Environnement.Requires bridge;
    
    private final Environnement implementation;
    
    public void start() {
      this.implementation.start();
      this.implementation.started = true;
    }
    
    protected void initParts() {
      
    }
    
    private void init_interagir() {
      assert this.interagir == null: "This is a bug.";
      this.interagir = this.implementation.make_interagir();
      if (this.interagir == null) {
      	throw new RuntimeException("make_interagir() in clrobots.Environnement should not return null.");
      }
    }
    
    private void init_envInfos() {
      assert this.envInfos == null: "This is a bug.";
      this.envInfos = this.implementation.make_envInfos();
      if (this.envInfos == null) {
      	throw new RuntimeException("make_envInfos() in clrobots.Environnement should not return null.");
      }
    }
    
    private void init_envInit() {
      assert this.envInit == null: "This is a bug.";
      this.envInit = this.implementation.make_envInit();
      if (this.envInit == null) {
      	throw new RuntimeException("make_envInit() in clrobots.Environnement should not return null.");
      }
    }
    
    protected void initProvidedPorts() {
      init_interagir();
      init_envInfos();
      init_envInit();
    }
    
    public ComponentImpl(final Environnement implem, final Environnement.Requires b, final boolean doInits) {
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
    
    private Iinteragir interagir;
    
    public Iinteragir interagir() {
      return this.interagir;
    }
    
    private IEnvInfos envInfos;
    
    public IEnvInfos envInfos() {
      return this.envInfos;
    }
    
    private IEnvInit envInit;
    
    public IEnvInit envInit() {
      return this.envInit;
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
  
  private Environnement.ComponentImpl selfComponent;
  
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
  protected Environnement.Provides provides() {
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
  protected abstract Iinteragir make_interagir();
  
  /**
   * This should be overridden by the implementation to define the provided port.
   * This will be called once during the construction of the component to initialize the port.
   * 
   */
  protected abstract IEnvInfos make_envInfos();
  
  /**
   * This should be overridden by the implementation to define the provided port.
   * This will be called once during the construction of the component to initialize the port.
   * 
   */
  protected abstract IEnvInit make_envInit();
  
  /**
   * This can be called by the implementation to access the required ports.
   * 
   */
  protected Environnement.Requires requires() {
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
  protected Environnement.Parts parts() {
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
  public synchronized Environnement.Component _newComponent(final Environnement.Requires b, final boolean start) {
    if (this.init) {
    	throw new RuntimeException("This instance of Environnement has already been used to create a component, use another one.");
    }
    this.init = true;
    Environnement.ComponentImpl  _comp = new Environnement.ComponentImpl(this, b, true);
    if (start) {
    	_comp.start();
    }
    return _comp;
  }
}
