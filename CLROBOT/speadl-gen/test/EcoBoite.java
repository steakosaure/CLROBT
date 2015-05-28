package test;

import clrobots.interfaces.IBoiteInfo;
import clrobots.interfaces.ICreateBoite;

@SuppressWarnings("all")
public abstract class EcoBoite {
  public interface Requires {
  }
  
  public interface Component extends EcoBoite.Provides {
  }
  
  public interface Provides {
    /**
     * This can be called to access the provided port.
     * 
     */
    public IBoiteInfo boitesInfos();
    
    /**
     * This can be called to access the provided port.
     * 
     */
    public ICreateBoite boiteCreate();
  }
  
  public interface Parts {
  }
  
  public static class ComponentImpl implements EcoBoite.Component, EcoBoite.Parts {
    private final EcoBoite.Requires bridge;
    
    private final EcoBoite implementation;
    
    public void start() {
      this.implementation.start();
      this.implementation.started = true;
    }
    
    protected void initParts() {
      
    }
    
    private void init_boitesInfos() {
      assert this.boitesInfos == null: "This is a bug.";
      this.boitesInfos = this.implementation.make_boitesInfos();
      if (this.boitesInfos == null) {
      	throw new RuntimeException("make_boitesInfos() in test.EcoBoite should not return null.");
      }
    }
    
    private void init_boiteCreate() {
      assert this.boiteCreate == null: "This is a bug.";
      this.boiteCreate = this.implementation.make_boiteCreate();
      if (this.boiteCreate == null) {
      	throw new RuntimeException("make_boiteCreate() in test.EcoBoite should not return null.");
      }
    }
    
    protected void initProvidedPorts() {
      init_boitesInfos();
      init_boiteCreate();
    }
    
    public ComponentImpl(final EcoBoite implem, final EcoBoite.Requires b, final boolean doInits) {
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
    
    private IBoiteInfo boitesInfos;
    
    public IBoiteInfo boitesInfos() {
      return this.boitesInfos;
    }
    
    private ICreateBoite boiteCreate;
    
    public ICreateBoite boiteCreate() {
      return this.boiteCreate;
    }
  }
  
  public static class BoiteSpecies {
    public interface Requires {
    }
    
    public interface Component extends EcoBoite.BoiteSpecies.Provides {
    }
    
    public interface Provides {
    }
    
    public interface Parts {
    }
    
    public static class ComponentImpl implements EcoBoite.BoiteSpecies.Component, EcoBoite.BoiteSpecies.Parts {
      private final EcoBoite.BoiteSpecies.Requires bridge;
      
      private final EcoBoite.BoiteSpecies implementation;
      
      public void start() {
        this.implementation.start();
        this.implementation.started = true;
      }
      
      protected void initParts() {
        
      }
      
      protected void initProvidedPorts() {
        
      }
      
      public ComponentImpl(final EcoBoite.BoiteSpecies implem, final EcoBoite.BoiteSpecies.Requires b, final boolean doInits) {
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
    
    private EcoBoite.BoiteSpecies.ComponentImpl selfComponent;
    
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
    protected EcoBoite.BoiteSpecies.Provides provides() {
      assert this.selfComponent != null: "This is a bug.";
      if (!this.init) {
      	throw new RuntimeException("provides() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if provides() is needed to initialise the component.");
      }
      return this.selfComponent;
    }
    
    /**
     * This can be called by the implementation to access the required ports.
     * 
     */
    protected EcoBoite.BoiteSpecies.Requires requires() {
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
    protected EcoBoite.BoiteSpecies.Parts parts() {
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
    public synchronized EcoBoite.BoiteSpecies.Component _newComponent(final EcoBoite.BoiteSpecies.Requires b, final boolean start) {
      if (this.init) {
      	throw new RuntimeException("This instance of BoiteSpecies has already been used to create a component, use another one.");
      }
      this.init = true;
      EcoBoite.BoiteSpecies.ComponentImpl  _comp = new EcoBoite.BoiteSpecies.ComponentImpl(this, b, true);
      if (start) {
      	_comp.start();
      }
      return _comp;
    }
    
    private EcoBoite.ComponentImpl ecosystemComponent;
    
    /**
     * This can be called by the species implementation to access the provided ports of its ecosystem.
     * 
     */
    protected EcoBoite.Provides eco_provides() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent;
    }
    
    /**
     * This can be called by the species implementation to access the required ports of its ecosystem.
     * 
     */
    protected EcoBoite.Requires eco_requires() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent.bridge;
    }
    
    /**
     * This can be called by the species implementation to access the parts of its ecosystem and their provided ports.
     * 
     */
    protected EcoBoite.Parts eco_parts() {
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
  
  private EcoBoite.ComponentImpl selfComponent;
  
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
  protected EcoBoite.Provides provides() {
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
  protected abstract IBoiteInfo make_boitesInfos();
  
  /**
   * This should be overridden by the implementation to define the provided port.
   * This will be called once during the construction of the component to initialize the port.
   * 
   */
  protected abstract ICreateBoite make_boiteCreate();
  
  /**
   * This can be called by the implementation to access the required ports.
   * 
   */
  protected EcoBoite.Requires requires() {
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
  protected EcoBoite.Parts parts() {
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
  public synchronized EcoBoite.Component _newComponent(final EcoBoite.Requires b, final boolean start) {
    if (this.init) {
    	throw new RuntimeException("This instance of EcoBoite has already been used to create a component, use another one.");
    }
    this.init = true;
    EcoBoite.ComponentImpl  _comp = new EcoBoite.ComponentImpl(this, b, true);
    if (start) {
    	_comp.start();
    }
    return _comp;
  }
  
  /**
   * This should be overridden by the implementation to instantiate the implementation of the species.
   * 
   */
  protected EcoBoite.BoiteSpecies make_BoiteSpecies() {
    return new EcoBoite.BoiteSpecies();
  }
  
  /**
   * Do not call, used by generated code.
   * 
   */
  public EcoBoite.BoiteSpecies _createImplementationOfBoiteSpecies() {
    EcoBoite.BoiteSpecies implem = make_BoiteSpecies();
    if (implem == null) {
    	throw new RuntimeException("make_BoiteSpecies() in test.EcoBoite should not return null.");
    }
    assert implem.ecosystemComponent == null: "This is a bug.";
    assert this.selfComponent != null: "This is a bug.";
    implem.ecosystemComponent = this.selfComponent;
    return implem;
  }
  
  /**
   * This can be called to create an instance of the species from inside the implementation of the ecosystem.
   * 
   */
  protected EcoBoite.BoiteSpecies.Component newBoiteSpecies() {
    EcoBoite.BoiteSpecies _implem = _createImplementationOfBoiteSpecies();
    return _implem._newComponent(new EcoBoite.BoiteSpecies.Requires() {},true);
  }
  
  /**
   * Use to instantiate a component from this implementation.
   * 
   */
  public EcoBoite.Component newComponent() {
    return this._newComponent(new EcoBoite.Requires() {}, true);
  }
}
