package clrobots;

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
    public ICreateBoite create();
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
    
    private void init_create() {
      assert this.create == null: "This is a bug.";
      this.create = this.implementation.make_create();
      if (this.create == null) {
      	throw new RuntimeException("make_create() in clrobots.EcoBoite should not return null.");
      }
    }
    
    protected void initProvidedPorts() {
      init_create();
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
    
    private ICreateBoite create;
    
    public ICreateBoite create() {
      return this.create;
    }
  }
  
  public static abstract class Boite {
    public interface Requires {
    }
    
    public interface Component extends EcoBoite.Boite.Provides {
    }
    
    public interface Provides {
      /**
       * This can be called to access the provided port.
       * 
       */
      public IBoiteInfo boiteinfo();
    }
    
    public interface Parts {
    }
    
    public static class ComponentImpl implements EcoBoite.Boite.Component, EcoBoite.Boite.Parts {
      private final EcoBoite.Boite.Requires bridge;
      
      private final EcoBoite.Boite implementation;
      
      public void start() {
        this.implementation.start();
        this.implementation.started = true;
      }
      
      protected void initParts() {
        
      }
      
      private void init_boiteinfo() {
        assert this.boiteinfo == null: "This is a bug.";
        this.boiteinfo = this.implementation.make_boiteinfo();
        if (this.boiteinfo == null) {
        	throw new RuntimeException("make_boiteinfo() in clrobots.EcoBoite$Boite should not return null.");
        }
      }
      
      protected void initProvidedPorts() {
        init_boiteinfo();
      }
      
      public ComponentImpl(final EcoBoite.Boite implem, final EcoBoite.Boite.Requires b, final boolean doInits) {
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
      
      private IBoiteInfo boiteinfo;
      
      public IBoiteInfo boiteinfo() {
        return this.boiteinfo;
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
    
    private EcoBoite.Boite.ComponentImpl selfComponent;
    
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
    protected EcoBoite.Boite.Provides provides() {
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
    protected abstract IBoiteInfo make_boiteinfo();
    
    /**
     * This can be called by the implementation to access the required ports.
     * 
     */
    protected EcoBoite.Boite.Requires requires() {
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
    protected EcoBoite.Boite.Parts parts() {
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
    public synchronized EcoBoite.Boite.Component _newComponent(final EcoBoite.Boite.Requires b, final boolean start) {
      if (this.init) {
      	throw new RuntimeException("This instance of Boite has already been used to create a component, use another one.");
      }
      this.init = true;
      EcoBoite.Boite.ComponentImpl  _comp = new EcoBoite.Boite.ComponentImpl(this, b, true);
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
  protected abstract ICreateBoite make_create();
  
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
  protected abstract EcoBoite.Boite make_Boite();
  
  /**
   * Do not call, used by generated code.
   * 
   */
  public EcoBoite.Boite _createImplementationOfBoite() {
    EcoBoite.Boite implem = make_Boite();
    if (implem == null) {
    	throw new RuntimeException("make_Boite() in clrobots.EcoBoite should not return null.");
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
  protected EcoBoite.Boite.Component newBoite() {
    EcoBoite.Boite _implem = _createImplementationOfBoite();
    return _implem._newComponent(new EcoBoite.Boite.Requires() {},true);
  }
  
  /**
   * Use to instantiate a component from this implementation.
   * 
   */
  public EcoBoite.Component newComponent() {
    return this._newComponent(new EcoBoite.Requires() {}, true);
  }
}
