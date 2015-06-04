package clrobots;

import clrobots.interfaces.CycleAlert;
import clrobots.interfaces.Do;

@SuppressWarnings("all")
public abstract class ForwardLauncher {
  public interface Requires {
    /**
     * This can be called by the implementation to access this required port.
     * 
     */
    public CycleAlert finishedCycle();
  }
  
  public interface Component extends ForwardLauncher.Provides {
  }
  
  public interface Provides {
    /**
     * This can be called to access the provided port.
     * 
     */
    public Do launchCycle();
  }
  
  public interface Parts {
  }
  
  public static class ComponentImpl implements ForwardLauncher.Component, ForwardLauncher.Parts {
    private final ForwardLauncher.Requires bridge;
    
    private final ForwardLauncher implementation;
    
    public void start() {
      this.implementation.start();
      this.implementation.started = true;
    }
    
    protected void initParts() {
      
    }
    
    private void init_launchCycle() {
      assert this.launchCycle == null: "This is a bug.";
      this.launchCycle = this.implementation.make_launchCycle();
      if (this.launchCycle == null) {
      	throw new RuntimeException("make_launchCycle() in clrobots.ForwardLauncher should not return null.");
      }
    }
    
    protected void initProvidedPorts() {
      init_launchCycle();
    }
    
    public ComponentImpl(final ForwardLauncher implem, final ForwardLauncher.Requires b, final boolean doInits) {
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
    
    private Do launchCycle;
    
    public Do launchCycle() {
      return this.launchCycle;
    }
  }
  
  public static abstract class AgentForward {
    public interface Requires {
      /**
       * This can be called by the implementation to access this required port.
       * 
       */
      public Do launchCycle();
    }
    
    public interface Component extends ForwardLauncher.AgentForward.Provides {
    }
    
    public interface Provides {
      /**
       * This can be called to access the provided port.
       * 
       */
      public CycleAlert finishedCycle();
    }
    
    public interface Parts {
    }
    
    public static class ComponentImpl implements ForwardLauncher.AgentForward.Component, ForwardLauncher.AgentForward.Parts {
      private final ForwardLauncher.AgentForward.Requires bridge;
      
      private final ForwardLauncher.AgentForward implementation;
      
      public void start() {
        this.implementation.start();
        this.implementation.started = true;
      }
      
      protected void initParts() {
        
      }
      
      private void init_finishedCycle() {
        assert this.finishedCycle == null: "This is a bug.";
        this.finishedCycle = this.implementation.make_finishedCycle();
        if (this.finishedCycle == null) {
        	throw new RuntimeException("make_finishedCycle() in clrobots.ForwardLauncher$AgentForward should not return null.");
        }
      }
      
      protected void initProvidedPorts() {
        init_finishedCycle();
      }
      
      public ComponentImpl(final ForwardLauncher.AgentForward implem, final ForwardLauncher.AgentForward.Requires b, final boolean doInits) {
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
      
      private CycleAlert finishedCycle;
      
      public CycleAlert finishedCycle() {
        return this.finishedCycle;
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
    
    private ForwardLauncher.AgentForward.ComponentImpl selfComponent;
    
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
    protected ForwardLauncher.AgentForward.Provides provides() {
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
    protected abstract CycleAlert make_finishedCycle();
    
    /**
     * This can be called by the implementation to access the required ports.
     * 
     */
    protected ForwardLauncher.AgentForward.Requires requires() {
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
    protected ForwardLauncher.AgentForward.Parts parts() {
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
    public synchronized ForwardLauncher.AgentForward.Component _newComponent(final ForwardLauncher.AgentForward.Requires b, final boolean start) {
      if (this.init) {
      	throw new RuntimeException("This instance of AgentForward has already been used to create a component, use another one.");
      }
      this.init = true;
      ForwardLauncher.AgentForward.ComponentImpl  _comp = new ForwardLauncher.AgentForward.ComponentImpl(this, b, true);
      if (start) {
      	_comp.start();
      }
      return _comp;
    }
    
    private ForwardLauncher.ComponentImpl ecosystemComponent;
    
    /**
     * This can be called by the species implementation to access the provided ports of its ecosystem.
     * 
     */
    protected ForwardLauncher.Provides eco_provides() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent;
    }
    
    /**
     * This can be called by the species implementation to access the required ports of its ecosystem.
     * 
     */
    protected ForwardLauncher.Requires eco_requires() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent.bridge;
    }
    
    /**
     * This can be called by the species implementation to access the parts of its ecosystem and their provided ports.
     * 
     */
    protected ForwardLauncher.Parts eco_parts() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent;
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
  
  private ForwardLauncher.ComponentImpl selfComponent;
  
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
  protected ForwardLauncher.Provides provides() {
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
  protected abstract Do make_launchCycle();
  
  /**
   * This can be called by the implementation to access the required ports.
   * 
   */
  protected ForwardLauncher.Requires requires() {
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
  protected ForwardLauncher.Parts parts() {
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
  public synchronized ForwardLauncher.Component _newComponent(final ForwardLauncher.Requires b, final boolean start) {
    if (this.init) {
    	throw new RuntimeException("This instance of ForwardLauncher has already been used to create a component, use another one.");
    }
    this.init = true;
    ForwardLauncher.ComponentImpl  _comp = new ForwardLauncher.ComponentImpl(this, b, true);
    if (start) {
    	_comp.start();
    }
    return _comp;
  }
  
  /**
   * This should be overridden by the implementation to instantiate the implementation of the species.
   * 
   */
  protected abstract ForwardLauncher.AgentForward make_AgentForward();
  
  /**
   * Do not call, used by generated code.
   * 
   */
  public ForwardLauncher.AgentForward _createImplementationOfAgentForward() {
    ForwardLauncher.AgentForward implem = make_AgentForward();
    if (implem == null) {
    	throw new RuntimeException("make_AgentForward() in clrobots.ForwardLauncher should not return null.");
    }
    assert implem.ecosystemComponent == null: "This is a bug.";
    assert this.selfComponent != null: "This is a bug.";
    implem.ecosystemComponent = this.selfComponent;
    return implem;
  }
}
